package com.example.ticketservice.controller;

import com.example.ticketservice.dto.response.AdminReviewResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews/admin-page/{ticketId}")
    public ResponseEntity<BaseResponseDto<AdminReviewResponseDto>> getAdminReviewList(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getAdminReviewList(ticketId)));
    }
}
