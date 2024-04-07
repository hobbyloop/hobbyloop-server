package com.example.ticketservice.fixture;

import com.example.ticketservice.dto.request.ReviewRequestDto;

public class ReviewFixture {

    public static final float BAD_SCORE = 1.0f;
    public static final float GOOD_SCORE = 5.0f;
    public static final float NORMAL_SCORE = 3.0f;

    public static ReviewRequestDto badReviewCreateRequest() {
        return new ReviewRequestDto(BAD_SCORE, "별로에요.");
    }

    public static ReviewRequestDto goodReviewCreateRequest() {
        return new ReviewRequestDto(GOOD_SCORE, "좋아요.");
    }

    public static ReviewRequestDto normalReviewCreateRequest() {
        return new ReviewRequestDto(NORMAL_SCORE, "나쁘지 않아요.");
    }
}
