package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.ticket.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.ticket.service.TicketService;
import com.example.ticketservice.ticket.service.UserTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tickets")
@Tag(name = "관리자 이용권 관리 API")
public class AdminTicketController {
    private final TicketService ticketService;

    @GetMapping("/management/{centerId}/{ticketId}")
    @Operation(summary = "이용권 목록")
    public ResponseEntity<BaseResponseDto<List<TicketResponseDto>>> getTicketList(
            @Parameter(description = "업체 아이디", required = true)
            @PathVariable(value = "centerId") long centerId,
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketList(centerId, ticketId)));
    }

    @PostMapping("/management/{centerId}")
    @Operation(summary = "이용권 등록")
    public ResponseEntity<BaseResponseDto<TicketCreateResponseDto>> createTicket(
            @Parameter(description = "업체 아이디", required = true)
            @PathVariable(value = "centerId") long centerId,
            @RequestPart(value = "requestDto") TicketCreateRequestDto requestDto,
            @Parameter(description = "이용권 이미지", required = true)
            @RequestPart(value = "ticketImage") MultipartFile ticketImage) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(ticketService.createTicket(centerId, requestDto, ticketImage)));
    }

    @GetMapping("/management/{ticketId}")
    @Operation(summary = "이용권 상세")
    public ResponseEntity<BaseResponseDto<TicketDetailResponseDto>> getTicketDetail(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketDetail(ticketId)));
    }

    @PatchMapping("/management/{ticketId}")
    @Operation(summary = "이용권 수정")
    public ResponseEntity<BaseResponseDto<Void>> updateTicket(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId,
            @RequestPart(value = "requestDto") TicketUpdateRequestDto requestDto,
            @Parameter(description = "이용권 이미지", required = true)
            @RequestPart(value = "ticketImage") MultipartFile ticketImage) {

        ticketService.updateTicket(ticketId, requestDto, ticketImage);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PatchMapping("/management/{ticketId}/upload")
    @Operation(summary = "이용권 업로드")
    public ResponseEntity<BaseResponseDto<Void>> uploadTicket(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        ticketService.uploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PatchMapping("/management/{ticketId}/cancel-upload")
    @Operation(summary = "이용권 업로드 취소")
    public ResponseEntity<BaseResponseDto<Void>> cancelUploadTicket(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        ticketService.cancelUploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @GetMapping("/review/{centerId}/{ticketId}")
    @Operation(summary = "시설 관리 예약 리뷰 페이지 - 이용권 목록")
    public ResponseEntity<BaseResponseDto<List<AdminTicketResponseDto>>> getAdminTicketList(
            @Parameter(description = "시설 아이디", required = true)
            @PathVariable(value = "centerId") long centerId,
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getAdminTicketList(centerId, ticketId)));
    }

    @GetMapping("/review/{ticketId}")
    @Operation(summary = "시설 관리자 페이지 리뷰 조회 시 이용권 정보 조회")
    public ResponseEntity<BaseResponseDto<AdminReviewTicketResponseDto>> getTicketInfo(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketInfo(ticketId)));
    }

    @GetMapping("/my/{centerId}")
    @Operation(summary = "관리자 내 정보 - 이용권 목록")
    public ResponseEntity<BaseResponseDto<List<AdminMyTicketResponseDto>>> getMyTicketList(
            @Parameter(description = "시설 아이디", required = true)
            @PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getMyTicketList(centerId)));
    }
}
