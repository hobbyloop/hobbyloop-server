package com.example.ticketservice.fixture;

import com.example.ticketservice.ticket.dto.request.ReviewRequestDto;

public class ReviewFixture {

    public static final float BAD_SCORE = 1.0f;
    public static final float GOOD_SCORE = 5.0f;
    public static final float NORMAL_SCORE = 3.0f;
    public static final int SORT_BY_SCORE_DESC = 0;
    public static final int SORT_BY_SCORE_ASC = 1;
    public static final int SORT_BY_REVIEW_ID_DESC = 2;
    public static final int SORT_BY_REVIEW_ID_ASC = 3;

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
