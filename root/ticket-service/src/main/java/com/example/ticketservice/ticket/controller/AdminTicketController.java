package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.ticket.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.ticket.service.TicketService;
import com.example.ticketservice.ticket.service.UserTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(summary = "이용권 목록", description = "한 페이지 당 20개\n[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=13786-23800&m=dev)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = TicketResponseDto.class)))
    public ResponseEntity<BaseResponseDto<List<TicketResponseDto>>> getTicketList(
            @Parameter(description = "업체 아이디", required = true)
            @PathVariable(value = "centerId") long centerId,
            @Parameter(description = "이용권 아이디, 최초 조회 시 -1, 무한스크롤 조회 시 마지막 이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketList(centerId, ticketId)));
    }

    @PostMapping(value = "/management/{centerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이용권 등록", description = "[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=13781-23634&m=dev)")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = TicketCreateResponseDto.class)))
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
    @Operation(summary = "이용권 상세", description = "[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=13791-23938&m=dev)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = TicketDetailResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<TicketDetailResponseDto>> getTicketDetail(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketDetail(ticketId)));
    }

    @PatchMapping(value = "/management/{ticketId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이용권 수정", description = "수정하지 않은 필드도 세팅해서 보내주세요\n[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=13786-23881&m=dev)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Void>> updateTicket(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId,
            @Parameter(description = "이용권 수정 요청 바디", required = true)
            @RequestPart(value = "requestDto") TicketUpdateRequestDto requestDto,
            @Parameter(description = "이용권 이미지", required = true)
            @RequestPart(value = "ticketImage") MultipartFile ticketImage) {

        ticketService.updateTicket(ticketId, requestDto, ticketImage);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PatchMapping("/management/{ticketId}/upload")
    @Operation(summary = "이용권 업로드", description = "업로드 후 판매 가능")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Void>> uploadTicket(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        ticketService.uploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PatchMapping("/management/{ticketId}/cancel-upload")
    @Operation(summary = "이용권 업로드 취소")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION,
            ExceptionEnum.TICKET_CANNOT_CANCEL_UPLOAD
    })
    public ResponseEntity<BaseResponseDto<Void>> cancelUploadTicket(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        ticketService.cancelUploadTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @GetMapping("/review/{centerId}/{ticketId}")
    @Operation(summary = "시설 관리 예약 리뷰 페이지 - 이용권 목록", description = "한 페이지 당 20개\n[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=14001-28093&m=dev)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdminTicketResponseDto.class)))
    public ResponseEntity<BaseResponseDto<List<AdminTicketResponseDto>>> getAdminTicketList(
            @Parameter(description = "시설 아이디", required = true)
            @PathVariable(value = "centerId") long centerId,
            @Parameter(description = "이용권 아이디, 최초 조회시 -1, 무한스크롤 조회 시 마지막 이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getAdminTicketList(centerId, ticketId)));
    }

    @GetMapping("/review/{ticketId}")
    @Operation(summary = "시설 관리자 페이지 리뷰 조회 시 이용권 정보 조회", description = "[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=14001-28285&m=dev)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdminReviewTicketResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<AdminReviewTicketResponseDto>> getTicketInfo(
            @Parameter(description = "이용권 아이디", required = true)
            @PathVariable(value = "ticketId") long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketInfo(ticketId)));
    }

    @GetMapping("/my/{centerId}")
    @Operation(summary = "관리자 내 정보 - 이용권 목록", description = "[피그마 링크](https://www.figma.com/design/nYEBH6aqCI37ZX0X6w7Ena/%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_Team-share?node-id=13967-26954&m=dev)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdminMyTicketResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없음")
    })
    public ResponseEntity<BaseResponseDto<List<AdminMyTicketResponseDto>>> getMyTicketList(
            @Parameter(description = "시설 아이디", required = true)
            @PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getMyTicketList(centerId)));
    }
}
