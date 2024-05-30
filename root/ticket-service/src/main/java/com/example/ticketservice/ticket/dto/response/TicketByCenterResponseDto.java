package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketByCenterResponseDto {
    private Long ticketId;

    private String ticketName;

    private int price;

    private int discountRate;

    private int calculatedPrice;

    public static TicketByCenterResponseDto from(Ticket ticket) {
        return TicketByCenterResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketName(ticket.getName())
                .price(ticket.getPrice())
                .discountRate(ticket.getDiscountRate())
                .calculatedPrice(ticket.getCalculatedPrice())
                .build();
    }
}
