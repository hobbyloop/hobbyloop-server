package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCreateResponseDto {
    private long ticketId;

    private String ticketImageUrl;

    private String centerName;

    private String ticketName;

    private String address;

    private int totalCount;

    private int useCount;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    public static TicketCreateResponseDto of(CenterInfoResponseDto centerInfo, Ticket ticket) {
        return TicketCreateResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .centerName(centerInfo.getCenterName())
                .address(centerInfo.getAddress())
                .ticketName(ticket.getName())
                .totalCount(ticket.getTotalCount())
                .useCount(ticket.getUseCount())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .build();
    }
}