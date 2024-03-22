package com.example.ticketservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListResponseDto {

    private float score;

    private List<String> totalImageUrlList;

    private List<ReviewResponseDto> reviewResponseDtoList;

    public static ReviewListResponseDto of(float score,
                                           List<String> totalImageUrlList,
                                           List<ReviewResponseDto> reviewResponseDtoList) {
        return ReviewListResponseDto.builder()
                .score(score)
                .totalImageUrlList(totalImageUrlList)
                .reviewResponseDtoList(reviewResponseDtoList)
                .build();
    }
}
