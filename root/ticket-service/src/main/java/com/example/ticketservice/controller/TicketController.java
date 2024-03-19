package com.example.ticketservice.controller;

import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.TicketCreateResponseDto;
import com.example.ticketservice.dto.response.AdminTicketResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.example.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/tickets/{centerId}")
    public ResponseEntity<BaseResponseDto<List<TicketResponseDto>>> getTicketList(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketList(centerId)));
    }

    @GetMapping("/tickets/admin-page/{centerId}")
    public ResponseEntity<BaseResponseDto<List<AdminTicketResponseDto>>> getAdminTicketList(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getAdminTicketList(centerId)));
    }

    @PostMapping("/tickets/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketCreateResponseDto>> createTicket(@PathVariable long centerId,
                                                                                 @RequestBody TicketCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(ticketService.createTicket(centerId, requestDto)));
    }
}
