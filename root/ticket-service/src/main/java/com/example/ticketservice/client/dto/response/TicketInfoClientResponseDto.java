package com.example.ticketservice.client.dto.response;

import com.example.ticketservice.entity.CategoryEnum;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketInfoClientResponseDto {

    private Long ticketId;

    private String ticketName;

    private float score;

    private int reviewCount;

    private String category;

    private int calculatedPrice;

    public static TicketInfoClientResponseDto of(Ticket ticket, float score, int reviewCount) {
        return TicketInfoClientResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketName(ticket.getName())
                .category(CategoryEnum.findByValue(ticket.getCategory()).getName())
                .calculatedPrice(ticket.getCalculatedPrice())
                .score(score)
                .reviewCount(reviewCount)
                .build();
    }
}
