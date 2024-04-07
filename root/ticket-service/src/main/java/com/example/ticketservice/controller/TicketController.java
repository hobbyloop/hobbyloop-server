package com.example.ticketservice.controller;

import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/admin/tickets/management/{centerId}/{ticketId}")
    public ResponseEntity<BaseResponseDto<List<TicketResponseDto>>> getTicketList(@PathVariable long centerId,
                                                                                  @PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketList(centerId, ticketId)));
    }

    @GetMapping("/admin/tickets/review/{centerId}/{ticketId}")
    public ResponseEntity<BaseResponseDto<List<AdminTicketResponseDto>>> getAdminTicketList(@PathVariable long centerId,
                                                                                            @PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getAdminTicketList(centerId, ticketId)));
    }

    @PostMapping("/admin/tickets/management/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketCreateResponseDto>> createTicket(@PathVariable long centerId,
                                                                                 @RequestPart TicketCreateRequestDto requestDto,
                                                                                 @RequestPart(required = false) MultipartFile ticketImage) {
        log.info(requestDto.getCategory());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(ticketService.createTicket(centerId, requestDto, ticketImage)));
    }

    @PostMapping("/tickets/bookmarks")
    public ResponseEntity<BaseResponseDto<Map<Long, BookmarkScoreTicketResponseDto>>> getBookmarkTicketList(@RequestBody List<Long> centerIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getBookmarkTicketList(centerIdList)));
    }

    @GetMapping("/admin/tickets/review/{ticketId}")
    public ResponseEntity<BaseResponseDto<AdminReviewTicketResponseDto>> getTicketInfo(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketInfo(ticketId)));
    }

    @GetMapping("/tickets/ios-review/{ticketId}")
    public ResponseEntity<BaseResponseDto<ReviewListTicketResponseDto>> getIOSTicketInfo(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getIOSTicketInfo(ticketId)));
    }

    @GetMapping("/admin/tickets/management/{ticketId}")
    public ResponseEntity<BaseResponseDto<TicketDetailResponseDto>> getTicketDetail(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketDetail(ticketId)));
    }

    @PatchMapping("/admin/tickets/management/{ticketId}")
    public ResponseEntity<BaseResponseDto<TicketDetailResponseDto>> updateTicket(@PathVariable long ticketId,
                                                                                @RequestPart TicketUpdateRequestDto requestDto,
                                                                                @RequestPart(required = false) MultipartFile ticketImage) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.updateTicket(ticketId, requestDto, ticketImage)));
    }

    @PatchMapping("/admin/tickets/management/{ticketId}/upload")
    public ResponseEntity<BaseResponseDto<Void>> uploadTicket(@PathVariable long ticketId) {
        ticketService.uploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PatchMapping("/admin/tickets/management/{ticketId}/cancel-upload")
    public ResponseEntity<BaseResponseDto<Void>> cancelUploadTicket(@PathVariable long ticketId) {
        ticketService.cancelUploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }
}
