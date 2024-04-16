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
public class BookmarkTicketResponseDto {

    private Long ticketId;

    private String name;

    private int calculatedPrice;

    private int duration;

    public static BookmarkTicketResponseDto from(Ticket ticket) {
        return BookmarkTicketResponseDto.builder()
                .ticketId(ticket.getId())
                .name(ticket.getName())
                .calculatedPrice(ticket.getCalculatedPrice())
                .duration(ticket.getDuration())
                .build();
    }
}
