package com.example.ticketservice.service;

import com.example.ticketservice.dto.response.AdminReviewResponseDto;

public interface ReviewService {
    AdminReviewResponseDto getAdminReviewList(long ticketId);
}
