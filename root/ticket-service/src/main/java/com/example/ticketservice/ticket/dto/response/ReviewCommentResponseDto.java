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
public class ReviewCommentResponseDto {

    @Schema(description = "회원 아이디", example = "1")
    private Long memberId;

    @Schema(description = "회원 닉네임", example = "하비")
    private String nickname;

    @Schema(description = "리뷰 아이디", example = "4")
    private Long reviewId;

    @Schema(description = "리뷰 별점", example = "3.5")
    private float score;

    @Schema(description = "리뷰 내용", example = "시설 좋아요!")
    private String content;

    @Schema(description = "리뷰 작성 시점", example = "2024-06-22T03:03:01.688Z")
    private LocalDateTime createdAt;

    @Schema(type = "array", description = "리뷰에 달린 댓글 목록")
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
