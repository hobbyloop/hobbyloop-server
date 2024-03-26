package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.ReviewRequestDto;
import com.example.ticketservice.dto.response.AdminReviewResponseDto;
import com.example.ticketservice.dto.response.ReviewListResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    AdminReviewResponseDto getAdminReviewList(long ticketId, long reviewId);

    Integer getReviewCountByCenterId(long centerId);

    ReviewListResponseDto getReviewList(long memberId, long ticketId, int pageNo, int sortId);

    Long createReview(long memberId, long ticketId, ReviewRequestDto requestDto, List<MultipartFile> reviewImageList);
}
