package com.example.ticketservice.dto.response;

import com.example.ticketservice.entity.Review;
import com.example.ticketservice.entity.Ticket;
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
public class ReviewResponseDto {

    private String nickname;

    private LocalDateTime createdAt;

    private float score;

    private List<String> reviewImageList;

    private String ticketName;

    private String content;

    private int commentCount;

    private int likeCount;

    private boolean isLike;

    public static ReviewResponseDto of(Review review,
                                       String ticketName,
                                       List<String> reviewImageList,
                                       boolean isLike) {
        return ReviewResponseDto.builder()
                .nickname(review.getNickname())
                .createdAt(review.getCreatedAt())
                .score(review.getScore())
                .reviewImageList(reviewImageList)
                .ticketName(ticketName)
                .content(review.getContent())
                .commentCount(review.getCommentCount())
                .likeCount(review.getLikeCount())
                .isLike(isLike)
                .build();
    }
}
