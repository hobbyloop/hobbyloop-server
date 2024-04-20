package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.CommentRequestDto;
import com.example.ticketservice.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    Long createComment(long companyId, long reviewId, CommentRequestDto requestDto);

    List<CommentResponseDto> getCommentByReviewId(long reviewId);
}
