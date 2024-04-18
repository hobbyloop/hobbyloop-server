package com.example.ticketservice.controller;

import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/ios-review/{ticketId}")
    public ResponseEntity<BaseResponseDto<ReviewListTicketResponseDto>> getIOSTicketInfo(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getIOSTicketInfo(ticketId)));
    }

    @GetMapping("/center/{centerId}")
    public ResponseEntity<BaseResponseDto<List<TicketByCenterResponseDto>>> getTicketListByCenter(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketListByCenter(centerId)));
    }
}
