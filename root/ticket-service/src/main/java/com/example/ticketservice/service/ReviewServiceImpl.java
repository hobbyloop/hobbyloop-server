package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.AdminReviewResponseDto;
import com.example.ticketservice.dto.response.ReviewCommentResponseDto;
import com.example.ticketservice.entity.Comment;
import com.example.ticketservice.entity.Review;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.CommentRepository;
import com.example.ticketservice.repository.ReviewRepository;
import com.example.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final TicketRepository ticketRepository;

    private final ReviewRepository reviewRepository;

    private final CompanyServiceClient companyServiceClient;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminReviewResponseDto getAdminReviewList(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        BaseResponseDto<CenterInfoResponseDto> centerInfo = companyServiceClient.getCenterInfo(ticket.getCenterId());
        List<ReviewCommentResponseDto> reviewCommentResponseDtoList = getReviewCommentResponseDtoList(ticketId);
        return AdminReviewResponseDto.of(centerInfo.getData(), ticket, reviewCommentResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getReviewCountByCenterId(long centerId) {
        return reviewRepository.countByCenterId(centerId);
    }

    private List<ReviewCommentResponseDto> getReviewCommentResponseDtoList(long ticketId) {
        List<ReviewCommentResponseDto> reviewCommentResponseDtoList = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAllByTicketId(ticketId);
        reviewList.forEach(r -> {
            List<Comment> commentList = commentRepository.findAllByReviewId(r.getId());
            List<String> commentString = commentList.stream().map(Comment::getContent).toList();
            ReviewCommentResponseDto reviewCommentResponseDto = ReviewCommentResponseDto.of(r, commentString);
            reviewCommentResponseDtoList.add(reviewCommentResponseDto);
        });
        return reviewCommentResponseDtoList;
    }
}
