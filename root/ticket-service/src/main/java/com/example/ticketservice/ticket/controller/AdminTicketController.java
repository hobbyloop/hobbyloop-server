package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.ticket.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.ticket.service.TicketService;
import com.example.ticketservice.ticket.service.UserTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tickets")
public class AdminTicketController {
    private final TicketService ticketService;

    @GetMapping("/management/{centerId}/{ticketId}")
    public ResponseEntity<BaseResponseDto<List<TicketResponseDto>>> getTicketList(@PathVariable(value = "centerId") long centerId,
                                                                                  @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketList(centerId, ticketId)));
    }

    @PostMapping("/management/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketCreateResponseDto>> createTicket(@PathVariable(value = "centerId") long centerId,
                                                                                 @RequestPart(value = "requestDto") TicketCreateRequestDto requestDto,
                                                                                 @RequestPart(value = "ticketImage") MultipartFile ticketImage) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(ticketService.createTicket(centerId, requestDto, ticketImage)));
    }

    @GetMapping("/management/{ticketId}")
    public ResponseEntity<BaseResponseDto<TicketDetailResponseDto>> getTicketDetail(@PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketDetail(ticketId)));
    }

    @PatchMapping("/management/{ticketId}")
    public ResponseEntity<BaseResponseDto<TicketDetailResponseDto>> updateTicket(@PathVariable(value = "ticketId") long ticketId,
                                                                                 @RequestPart(value = "requestDto") TicketUpdateRequestDto requestDto,
                                                                                 @RequestPart(value = "ticketImage") MultipartFile ticketImage) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.updateTicket(ticketId, requestDto, ticketImage)));
    }

    @PatchMapping("/management/{ticketId}/upload")
    public ResponseEntity<BaseResponseDto<Void>> uploadTicket(@PathVariable(value = "ticketId") long ticketId) {
        ticketService.uploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PatchMapping("/management/{ticketId}/cancel-upload")
    public ResponseEntity<BaseResponseDto<Void>> cancelUploadTicket(@PathVariable(value = "ticketId") long ticketId) {
        ticketService.cancelUploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @GetMapping("/review/{centerId}/{ticketId}")
    public ResponseEntity<BaseResponseDto<List<AdminTicketResponseDto>>> getAdminTicketList(@PathVariable(value = "centerId") long centerId,
                                                                                            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getAdminTicketList(centerId, ticketId)));
    }

    @GetMapping("/review/{ticketId}")
    public ResponseEntity<BaseResponseDto<AdminReviewTicketResponseDto>> getTicketInfo(@PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketInfo(ticketId)));
    }

    @GetMapping("/my/{centerId}")
    public ResponseEntity<BaseResponseDto<List<AdminMyTicketResponseDto>>> getMyTicketList(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getMyTicketList(centerId)));
    }
}
