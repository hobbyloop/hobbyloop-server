package com.example.ticketservice.ticket.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlindReviewRequestDto {

    private Long reviewId;

    private Long blindRequestId;
}
