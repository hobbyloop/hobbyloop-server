package com.example.ticketservice.ticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TicketReviewListByCenterResponseDto {
    private int reviewCount;
    private float score;
    private List<ReviewByCenterResponseDto> reviewList;
}
