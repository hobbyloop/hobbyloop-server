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
public class ReviewListTicketResponseDto {

    private float score;

    private List<String> totalImageUrlList;

    public static ReviewListTicketResponseDto of(float score,
                                                 List<String> totalImageUrlList) {
        return ReviewListTicketResponseDto.builder()
                .score(score)
                .totalImageUrlList(totalImageUrlList)
                .build();
    }
}
