package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "리뷰 아이디", example = "1")
    private Long reviewId;

    @Schema(description = "리뷰 작성자 닉네임", example = "루프")
    private String nickname;

    @Schema(description = "리뷰 작성 시점")
    private LocalDateTime createdAt;

    @Schema(description = "별점", example = "3.5")
    private float score;

    @Schema(description = "리뷰 이미지 목록", type = "array")
    private List<String> reviewImageList;

    @Schema(description = "이용권 이름", example = "2:1 필라테스 20회")
    private String ticketName;

    @Schema(description = "리뷰 내용", example = "시설 좋아요!")
    private String content;

    @Schema(description = "댓글 수", example = "2")
    private int commentCount;

    @Schema(description = "좋아요 수", example = "10")
    private int likeCount;

    @Schema(description = "좋아요 여부", example = "true")
    private boolean isLike;

    @Schema(description = "무시하셔도 됩니다")
    private boolean like;

    public static ReviewResponseDto of(Review review,
                                       String ticketName,
                                       List<String> reviewImageList,
                                       boolean isLike) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
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
