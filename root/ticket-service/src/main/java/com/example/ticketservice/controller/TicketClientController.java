package com.example.ticketservice.controller;

import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.client.dto.response.TicketClientResponseDto;
import com.example.ticketservice.service.TicketClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client/tickets")
public class TicketClientController {

    private final TicketClientService ticketClientService;

    @GetMapping("/{centerId}")
    public ResponseEntity<BaseResponseDto<List<TicketClientResponseDto>>> getTicketClientResponseDto(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getTicketClientResponseDto(centerId)));
    }

    @GetMapping("/detail/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketDetailClientResponseDto>> getTicketDetailInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getTicketDetailInfo(centerId)));
    }
}
