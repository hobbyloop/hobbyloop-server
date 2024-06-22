package com.example.ticketservice.ticket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListTicketResponseDto {

    @Schema(description = "평균 별점", example = "4.8")
    private float score;

    @Schema(type = "array", description = "전체 리뷰 이미지 모음")
    private List<String> totalImageUrlList;

    public static ReviewListTicketResponseDto of(float score,
                                                 List<String> totalImageUrlList) {
        return ReviewListTicketResponseDto.builder()
                .score(score)
                .totalImageUrlList(totalImageUrlList)
                .build();
    }
}
