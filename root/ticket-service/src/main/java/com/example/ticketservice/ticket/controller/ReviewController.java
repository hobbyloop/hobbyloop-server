package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.request.ReviewRequestDto;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewCommentResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketReviewListByCenterResponseDto;
import com.example.ticketservice.ticket.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "이용권 리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/reviews/{ticketId}",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "리뷰 작성",
                description = "이 엔드포인트는 multipart/form-data 형식의 요청을 받음")
    @ApiResponse(responseCode = "201", description = "성공 시 만들어진 리뷰 ID 리턴", content = @Content(schema = @Schema(implementation = Long.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Long>> createReview(
            HttpServletRequest request,
            @PathVariable long ticketId,
            @Parameter(description = "생성할 리뷰 정보(JSON)", required = true)
            @RequestPart ReviewRequestDto requestDto,
            @Parameter(description = "리뷰 이미지 목록", required = true)
            @RequestPart List<MultipartFile> reviewImageList) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(reviewService.createReview(memberId, ticketId, requestDto, reviewImageList)));
    }

    @GetMapping("/admin/reviews/{ticketId}/{reviewId}")
    @Operation(summary = "관리자 - 이용권별 리뷰 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewCommentResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<ReviewCommentResponseDto>>> getAdminReviewList(
            @PathVariable long ticketId,
            @Parameter(description = "최초 조회 시 -1, 무한스크롤 조회 시 마지막 리뷰 아이디", required = true)
            @PathVariable long reviewId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getAdminReviewList(ticketId, reviewId)));
    }

    @GetMapping("/reviews/count/{centerId}")
    @Operation(summary = "시설에 달린 리뷰 카운트 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Integer.class)))
    public ResponseEntity<BaseResponseDto<Integer>> getReviewCountByCenterId(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getReviewCountByCenterId(centerId)));
    }

    @GetMapping("/reviews/{ticketId}/{pageNo}/{sortId}")
    @Operation(summary = "이용권별 리뷰 목록 조회", description = "한 페이지 당 20개")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewResponseDto.class))))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<List<ReviewResponseDto>>> getReviewList(
            HttpServletRequest request,
            @PathVariable long ticketId,
            @Parameter(description = "페이지 번호", required = true)
            @PathVariable int pageNo,
            @Parameter(description = "정렬 기준, 0: 별점 높은 순, 1: 별점 낮은 순, 2: 최신순, 3: 오래된순", required = true)
            @PathVariable int sortId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getReviewList(memberId, ticketId, pageNo, sortId)));
    }

    @GetMapping("/centers/{centerId}/reviews/{pageNo}/{sortId}")
    @Operation(summary = "특정 시설 정보 상세 조회 - 리뷰 탭", description = "한 페이지 당 20개")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = TicketReviewListByCenterResponseDto.class)))
    public ResponseEntity<BaseResponseDto<TicketReviewListByCenterResponseDto>> getTicketReviewListByCenter(
            @PathVariable long centerId,
            @Parameter(description = "페이지 번호", required = true)
            @PathVariable int pageNo,
            @Parameter(description = "정렬 기준, 0: 별점 높은 순, 1: 별점 낮은 순, 2: 최신순, 3: 오래된순", required = true)
            @PathVariable int sortId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getReviewListByCenter(centerId, pageNo, sortId)));
    }
}
