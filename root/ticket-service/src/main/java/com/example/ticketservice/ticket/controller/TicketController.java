package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.security.RoleAuthorization;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.client.dto.response.TicketClientForLectureResponseDto;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.ticket.service.TicketService;
import com.example.ticketservice.ticket.dto.response.CategoryTicketResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewListTicketResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketByCenterResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
@Tag(name = "이용권 조회 관련 API")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/ios-review/{ticketId}")
    @Operation(summary = "iOS 이용권 별 리뷰 리스트 조회시 이용권 정보 조회", description = "[피그마 링크](https://www.figma.com/file/nYEBH6aqCI37ZX0X6w7Ena?embed_host=notion&kind=file&mode=design&node-id=11916-22888&t=6ekkLRISaKQTqVa6-0&type=design&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ReviewListTicketResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<ReviewListTicketResponseDto>> getIOSTicketInfo(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getIOSTicketInfo(ticketId)));
    }

    @GetMapping("/centers/{centerId}")
    @Operation(summary = "특정 시설 정보 상세 조회 - 이용권 탭", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-53729&t=da28ryPWiX4Q2W9O-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketByCenterResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<TicketByCenterResponseDto>>> getTicketListByCenter(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketListByCenter(centerId)));
    }

    @GetMapping ("/category/{category}/{sortId}/{refundable}/{score}/{allow-location}/{latitude}/{longitude}/{distance}")
    public ResponseEntity<BaseResponseDto<List<CategoryTicketResponseDto>>> getCategoryTicketAroundMe(HttpServletRequest request,
                                                                                              @PathVariable(value = "category") String category,
                                                                                              @PathVariable(value = "sortId") int sortId,
                                                                                              @PathVariable(value = "refundable") int refundable,
                                                                                              @PathVariable(value = "score") double score,
                                                                                              @PathVariable(value = "allow-location") int allowLocation,
                                                                                              @PathVariable(value = "latitude") double latitude,
                                                                                              @PathVariable(value = "longitude") double longitude,
                                                                                              @PathVariable(value = "distance") int distance) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getCategoryTicketAroundMe(memberId, category, sortId, refundable, score, allowLocation, latitude, longitude, distance)));
    }

    @GetMapping ("/category/{category}/{sortId}/{refundable}/{score}/{pageNo}")
    public ResponseEntity<BaseResponseDto<List<CategoryTicketResponseDto>>> getCategoryTicket(HttpServletRequest request,
                                                                                              @PathVariable(value = "category") String category,
                                                                                              @PathVariable(value = "sortId") int sortId,
                                                                                              @PathVariable(value = "refundable") int refundable,
                                                                                              @PathVariable(value = "score") double score,
                                                                                              @PathVariable(value = "pageNo") int pageNo,
                                                                                              @RequestParam(value = "location") List<String> locations) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getCategoryTicket(memberId, category, sortId, refundable, score, pageNo, locations)));
    }

    @GetMapping("/lecture/{centerId}")
    public ResponseEntity<BaseResponseDto<List<TicketClientForLectureResponseDto>>> getTicketListForLecture(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketListForLecture(centerId)));
    }
}
