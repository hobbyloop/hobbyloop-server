package com.example.ticketservice.service;

import com.example.ticketservice.dto.response.AdminReviewResponseDto;
import com.example.ticketservice.dto.response.ReviewListResponseDto;

public interface ReviewService {
    AdminReviewResponseDto getAdminReviewList(long ticketId);

    Integer getReviewCountByCenterId(long centerId);

    ReviewListResponseDto getReviewList(long memberId, long ticketId);
}
