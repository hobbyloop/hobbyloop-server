package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.security.RoleAuthorization;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.ticket.dto.response.RecentPurchaseUserTicketListResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.UserTicketExpiringHistoryResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.UserTicketUsingHistoryResponseDto;
import com.example.ticketservice.ticket.service.UserTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "최근에 구매한 이용권 목록 조회")
    public ResponseEntity<BaseResponseDto<Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>>>> getMyRecentPurchaseUserTicketList(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getRecentPurchaseUserTicketList(memberId)));
    }

    @GetMapping("/available")
    @RoleAuthorization(roles = {"USER"})
    public ResponseEntity<BaseResponseDto<List<AvailableUserTicketsWithCenterInfo>>> getMyAvailableUserTicketList(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getAvailableUserTicketList(memberId)));
    }

    @GetMapping("/using-histories")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "사용자 마이페이지 - 이용권 사용 내역")
    public ResponseEntity<BaseResponseDto<List<UserTicketUsingHistoryResponseDto>>> getUserTicketsUsingHistories(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getUserTicketUsingHistory(memberId)));
    }

    @GetMapping("/expiring-histories")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "사용자 마이페이지 - 이용권 소멸 내역")
    public ResponseEntity<BaseResponseDto<List<UserTicketExpiringHistoryResponseDto>>> getUserTicketExpiringHistories(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getUserTicketExpiringHistory(memberId)));
    }
}
