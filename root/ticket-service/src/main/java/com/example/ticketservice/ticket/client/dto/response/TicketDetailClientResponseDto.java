package com.example.ticketservice.ticket.client.dto.response;

import com.example.ticketservice.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetailClientResponseDto {

    private float score;

    private int reviewCount;

    private int price;

    private int discountRate;

    private int calculatedPrice;

    public static TicketDetailClientResponseDto of(float score, int reviewCount, Ticket ticket) {
        return TicketDetailClientResponseDto.builder()
                .score(score)
                .reviewCount(reviewCount)
                .price(ticket.getPrice())
                .discountRate(ticket.getDiscountRate())
                .calculatedPrice(ticket.getCalculatedPrice())
                .build();
    }
}
