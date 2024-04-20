package com.example.ticketservice.dto.response;

import com.example.ticketservice.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewByCenterResponseDto {
    private Long reviewId;

    private String nickname;

    private LocalDateTime createdAt;

    private float score;

    private String content;

    public static ReviewByCenterResponseDto from(Review review) {
        return ReviewByCenterResponseDto.builder()
                .reviewId(review.getId())
                .nickname(review.getNickname())
                .createdAt(review.getCreatedAt())
                .score(review.getScore())
                .content(review.getContent())
                .build();
    }
}
