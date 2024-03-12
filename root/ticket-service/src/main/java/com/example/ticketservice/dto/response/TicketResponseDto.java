package com.example.ticketservice.dto.response;

import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDto {

    private Long ticketId;

    private String name;

    private int price;

    private int duration;

    public static TicketResponseDto from(Ticket ticket) {
        return TicketResponseDto.builder()
                .ticketId(ticket.getId())
                .name(ticket.getName())
                .price(ticket.getPrice())
                .duration(ticket.getDuration())
                .build();
    }
}
