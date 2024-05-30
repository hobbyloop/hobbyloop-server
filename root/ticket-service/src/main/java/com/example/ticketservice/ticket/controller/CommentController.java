package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.ticket.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.CommentRequestDto;
import com.example.ticketservice.ticket.dto.response.CommentResponseDto;
import com.example.ticketservice.ticket.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{reviewId}")
    public ResponseEntity<BaseResponseDto<Long>> createComment(HttpServletRequest request,
                                                               @PathVariable long reviewId,
                                                               @RequestBody CommentRequestDto requestDto) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(commentService.createComment(companyId, reviewId, requestDto)));
    }

    @GetMapping("/comments/{reviewId}")
    public ResponseEntity<BaseResponseDto<List<CommentResponseDto>>> getCommentByReviewId(@PathVariable long reviewId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(commentService.getCommentByReviewId(reviewId)));
    }
}
