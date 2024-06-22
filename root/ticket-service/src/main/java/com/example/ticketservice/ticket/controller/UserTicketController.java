package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.security.RoleAuthorization;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.ticket.dto.response.RecentPurchaseUserTicketListResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.UserTicketExpiringHistoryResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.UserTicketUsingHistoryResponseDto;
import com.example.ticketservice.ticket.service.UserTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-tickets")
@Tag(name = "사용자 이용권 API", description = "사용자가 구매한 이용권 관련 API")
public class UserTicketController {
    private final UserTicketService userTicketService;

    @PostMapping("/{ticketId}/purchase")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "이용권 구매")
    @ApiResponse(responseCode = "201", description = "구매 성공 시 사용자에게 발급된 이용권 아이디 반환", content = @Content(schema = @Schema(implementation = Long.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION,
            ExceptionEnum.TICKET_NOT_UPLOAD_EXCEPTION,
            ExceptionEnum.TICKET_SOLD_OUT_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Long>> purchaseTicket(
            @Parameter(description = "구매할 이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId,
            HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(userTicketService.purchaseTicket(memberId, ticketId)));
    }

    @GetMapping("/recent-purchase")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "최근에 구매한 이용권 목록 조회", description = "[피그마 링크](https://www.figma.com/file/ShgCuih6scznAlHzHNz8Jo?embed_host=notion&kind=file&node-id=1687-57997&t=fhlLN5QWfjir2eZa-4&viewer=1)")
    public ResponseEntity<BaseResponseDto<Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>>>> getMyRecentPurchaseUserTicketList(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getRecentPurchaseUserTicketList(memberId)));
    }

    @GetMapping("/available")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "예약 가능한 이용권 목록 조회", description = "[피그마 링크](https://www.figma.com/file/nYEBH6aqCI37ZX0X6w7Ena?embed_host=notion&kind=file&mode=dev&node-id=13428-22479&type=design&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AvailableUserTicketsWithCenterInfo.class))))
    public ResponseEntity<BaseResponseDto<List<AvailableUserTicketsWithCenterInfo>>> getMyAvailableUserTicketList(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getAvailableUserTicketList(memberId)));
    }

    @GetMapping("/using-histories")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "사용자 마이페이지 - 이용권 사용 내역", description = "[피그마 링크](https://www.figma.com/file/ShgCuih6scznAlHzHNz8Jo?embed_host=notion&kind=file&node-id=1687-57997&t=fhlLN5QWfjir2eZa-4&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserTicketUsingHistoryResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<UserTicketUsingHistoryResponseDto>>> getUserTicketsUsingHistories(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getUserTicketUsingHistory(memberId)));
    }

    @GetMapping("/expiring-histories")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "사용자 마이페이지 - 이용권 소멸 내역", description = "[피그마 링크](https://www.figma.com/file/ShgCuih6scznAlHzHNz8Jo?embed_host=notion&kind=file&node-id=1687-57762&t=fhlLN5QWfjir2eZa-4&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserTicketExpiringHistoryResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<UserTicketExpiringHistoryResponseDto>>> getUserTicketExpiringHistories(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getUserTicketExpiringHistory(memberId)));
    }
}
