package com.example.companyservice.company.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlindReviewRequestDto {

    private Long reviewId;

    private Long blindRequestId;

    public static BlindReviewRequestDto of(Long reviewId, Long blindRequestId) {
        return BlindReviewRequestDto.builder()
                .reviewId(reviewId)
                .blindRequestId(blindRequestId)
                .build();
    }
}
