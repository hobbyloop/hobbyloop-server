package com.example.ticketservice.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/tickets/bookmarks")
    public ResponseEntity<BaseResponseDto<Map<Long, BookmarkScoreTicketResponseDto>>> getBookmarkTicketList(@RequestBody List<Long> centerIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getBookmarkTicketList(centerIdList)));
    }

    @GetMapping("/tickets/ios-review/{ticketId}")
    public ResponseEntity<BaseResponseDto<ReviewListTicketResponseDto>> getIOSTicketInfo(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getIOSTicketInfo(ticketId)));
    }

    @PostMapping("/tickets/{ticketId}/purchase")
    public ResponseEntity<BaseResponseDto<Long>> purchaseTicket(@PathVariable long ticketId,
                                                                HttpServletRequest request) {

        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(ticketService.purchaseTicket(ticketId, memberId)));
    }
}
