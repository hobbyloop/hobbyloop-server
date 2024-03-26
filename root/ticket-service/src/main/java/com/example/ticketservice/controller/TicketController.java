package com.example.ticketservice.controller;

import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.BookmarkTicketResponseDto;
import com.example.ticketservice.dto.response.TicketCreateResponseDto;
import com.example.ticketservice.dto.response.AdminTicketResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.example.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/tickets/admin-page/{centerId}/{ticketId}")
    public ResponseEntity<BaseResponseDto<List<AdminTicketResponseDto>>> getAdminTicketList(@PathVariable long centerId,
                                                                                            @PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getAdminTicketList(centerId, ticketId)));
    }

    @PostMapping("/tickets/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketCreateResponseDto>> createTicket(@PathVariable long centerId,
                                                                                 @RequestPart TicketCreateRequestDto requestDto,
                                                                                 @RequestPart(required = false) MultipartFile ticketImage) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(ticketService.createTicket(centerId, requestDto, ticketImage)));
    }

    @PostMapping("/tickets/bookmark-center")
    public ResponseEntity<BaseResponseDto<Map<Long, List<BookmarkTicketResponseDto>>>> getBookmarkTicketList(@RequestBody List<Long> centerIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getBookmarkTicketList(centerIdList)));
    }
}
