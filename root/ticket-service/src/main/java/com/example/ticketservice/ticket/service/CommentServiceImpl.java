package com.example.ticketservice.ticket.service;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.ticket.dto.request.CommentRequestDto;
import com.example.ticketservice.ticket.dto.response.CommentResponseDto;
import com.example.ticketservice.ticket.entity.Comment;
import com.example.ticketservice.ticket.entity.Review;
import com.example.ticketservice.ticket.repository.CommentRepository;
import com.example.ticketservice.ticket.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ReviewRepository reviewRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Long createComment(long companyId, long reviewId, CommentRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.REVIEW_NOT_EXIST_EXCEPTION));
        Comment comment = Comment.of(requestDto.getContent(), review, companyId);
        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentByReviewId(long reviewId) {
        List<Comment> commentList = commentRepository.findAllByReviewIdOrderByCreatedAt(reviewId);
        return commentList.stream().map(c -> new CommentResponseDto(c.getContent())).toList();
    }
}
