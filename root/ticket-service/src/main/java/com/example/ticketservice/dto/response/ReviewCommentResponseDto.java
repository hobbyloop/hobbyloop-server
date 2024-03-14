package com.example.ticketservice.dto.response;

import com.example.ticketservice.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommentResponseDto {

    private Long memberId;

    private String nickname;

    private Long reviewId;

    private float score;

    private String content;

    private LocalDateTime createdAt;

    private List<String> commentResponseDtoList;

    public static ReviewCommentResponseDto of(Review review, List<String> commentResponseDtoList) {
        return ReviewCommentResponseDto.builder()
                .memberId(review.getMemberId())
                .nickname(review.getNickname())
                .reviewId(review.getId())
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .commentResponseDtoList(commentResponseDtoList)
                .build();
    }
}
