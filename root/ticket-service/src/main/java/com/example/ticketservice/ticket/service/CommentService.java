package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.dto.request.CommentRequestDto;
import com.example.ticketservice.ticket.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    Long createComment(long companyId, long reviewId, CommentRequestDto requestDto);

    List<CommentResponseDto> getCommentByReviewId(long reviewId);
}
