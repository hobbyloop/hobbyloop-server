package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.CommentRequestDto;
import com.example.ticketservice.ticket.dto.response.CommentResponseDto;
import com.example.ticketservice.ticket.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "이용권 리뷰 댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{reviewId}")
    @Operation(summary = "댓글 등록")
    @ApiResponse(responseCode = "201", description = "성공 시 댓글의 아이디 반환", content = @Content(schema = @Schema(implementation = Long.class)))
    @ApiExceptionResponse({
            ExceptionEnum.REVIEW_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Long>> createComment(HttpServletRequest request,
                                                               @PathVariable long reviewId,
                                                               @RequestBody CommentRequestDto requestDto) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(commentService.createComment(companyId, reviewId, requestDto)));
    }

    @GetMapping("/comments/{reviewId}")
    @Operation(summary = "리뷰 댓글 목록 조회", description = "[피그마 링크](https://www.figma.com/file/nYEBH6aqCI37ZX0X6w7Ena?embed_host=notion&kind=file&mode=design&node-id=9914-20177&t=2MJ4808m7Cn3L1Wd-0&type=design&viewer=1)")
    public ResponseEntity<BaseResponseDto<List<CommentResponseDto>>> getCommentByReviewId(@PathVariable long reviewId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(commentService.getCommentByReviewId(reviewId)));
    }
}
