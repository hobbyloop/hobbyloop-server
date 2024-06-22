package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTicketResponseDto {

    private Long centerId;

    private boolean isRefundable;

    private boolean isBookmark;

    private String logoImageUrl;

    private String centerName;

    private String address;

    private int calculatedPrice;

    private float score;

    private int reviewCount;

    public static CategoryTicketResponseDto of(Ticket ticket,
                                               boolean isBookmark,
                                               float score,
                                               int reviewCount) {
        return CategoryTicketResponseDto.builder()
                .centerId(ticket.getCenterId())
                .isRefundable(ticket.isRefundable())
                .isBookmark(isBookmark)
                .logoImageUrl(ticket.getLogoImageUrl())
                .centerName(ticket.getCenterName())
                .address(ticket.getAddress())
                .calculatedPrice(ticket.getCalculatedPrice())
                .score(score)
                .reviewCount(reviewCount)
                .build();
    }
}
