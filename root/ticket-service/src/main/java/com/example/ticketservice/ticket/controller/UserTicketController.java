package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.ticket.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.ticket.dto.response.RecentPurchaseUserTicketListResponseDto;
import com.example.ticketservice.ticket.service.UserTicketService;
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
public class UserTicketController {
    private final UserTicketService userTicketService;

    @PostMapping("/{ticketId}/purchase")
    public ResponseEntity<BaseResponseDto<Long>> purchaseTicket(@PathVariable(value = "ticketId") long ticketId,
                                                                HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(userTicketService.purchaseTicket(memberId, ticketId)));
    }

    @GetMapping("/recent-purchase")
    public ResponseEntity<BaseResponseDto<Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>>>> getMyRecentPurchaseUserTicketList(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getRecentPurchaseUserTicketList(memberId)));
    }

    @GetMapping("/available")
    public ResponseEntity<BaseResponseDto<Map<String, AvailableUserTicketsWithCenterInfo>>> getMyAvailableUserTicketList(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getAvailableUserTicketList(memberId)));
    }
}
