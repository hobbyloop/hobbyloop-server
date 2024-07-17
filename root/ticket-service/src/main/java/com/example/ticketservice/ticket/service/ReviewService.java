package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.dto.request.ReviewRequestDto;
import com.example.ticketservice.ticket.dto.response.ReviewCommentResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketReviewListByCenterResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    List<ReviewCommentResponseDto> getAdminReviewList(long ticketId, long reviewId);

    Integer getReviewCountByCenterId(long centerId);

    List<ReviewResponseDto> getReviewList(long memberId, long ticketId, int pageNo, int sortId);

    Long createReview(long memberId, long ticketId, ReviewRequestDto requestDto, List<MultipartFile> reviewImageList);

    TicketReviewListByCenterResponseDto getReviewListByCenter(long centerId, int pageNo, int sortId);

    float getScore(long ticketId);

    void blindReview(long reviewId);
}
