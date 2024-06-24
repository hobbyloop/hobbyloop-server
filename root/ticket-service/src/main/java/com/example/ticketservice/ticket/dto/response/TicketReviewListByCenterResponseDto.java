package com.example.ticketservice.ticket.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TicketReviewListByCenterResponseDto {
    @Schema(description = "이용권에 달린 리뷰 수", example = "152")
    private int reviewCount;

    @Schema(description = "리뷰 평균 별점", example = "4.2")
    private float score;

    @ArraySchema(schema = @Schema(implementation = ReviewByCenterResponseDto.class))
    private List<ReviewByCenterResponseDto> reviewList;
}
