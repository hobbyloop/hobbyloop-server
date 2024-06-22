package com.example.ticketservice.ticket.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @Schema(description = "리뷰 별점", example = "3.5")
    private float score;

    @Schema(description = "리뷰 내용", example = "시설 좋아요!")
    private String content;
}
