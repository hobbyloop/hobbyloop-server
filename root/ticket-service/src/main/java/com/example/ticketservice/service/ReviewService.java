package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.ReviewRequestDto;
import com.example.ticketservice.dto.response.ReviewCommentResponseDto;
import com.example.ticketservice.dto.response.ReviewResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    List<ReviewCommentResponseDto> getAdminReviewList(long ticketId, long reviewId);

    Integer getReviewCountByCenterId(long centerId);

    List<ReviewResponseDto> getReviewList(long memberId, long ticketId, int pageNo, int sortId);

    Long createReview(long memberId, long ticketId, ReviewRequestDto requestDto, List<MultipartFile> reviewImageList);

    float getScore(long ticketId);
}
