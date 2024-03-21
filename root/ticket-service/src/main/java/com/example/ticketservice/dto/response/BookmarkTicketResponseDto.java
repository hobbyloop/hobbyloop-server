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

    private int price;

    private int discountRate;

    private int totalPrice;

    private int duration;

    public static BookmarkTicketResponseDto from(Ticket ticket) {
        return BookmarkTicketResponseDto.builder()
                .ticketId(ticket.getId())
                .name(ticket.getName())
                .price(ticket.getPrice())
                .discountRate(ticket.getDiscountRate())
                .totalPrice(ticket.getCalculatedPrice())
                .duration(ticket.getDuration())
                .build();
    }
}
