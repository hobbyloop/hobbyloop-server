package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "리뷰 아이디", example = "1")
    private Long reviewId;

    @Schema(description = "리뷰 작성자 닉네임", example = "필라")
    private String nickname;

    @Schema(description = "리뷰 작성 시점")
    private LocalDateTime createdAt;

    @Schema(description = "별점", example = "4.5")
    private float score;

    @Schema(description = "리뷰 내용", example = "시설 좋아요!")
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
