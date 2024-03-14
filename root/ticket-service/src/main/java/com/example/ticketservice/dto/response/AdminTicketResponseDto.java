package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTicketResponseDto {

    private CenterInfoResponseDto centerInfo;

    private String name;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    private int totalCount;

    private int issueCount;

    public static AdminTicketResponseDto of(CenterInfoResponseDto centerInfo, Ticket ticket) {
        return AdminTicketResponseDto.builder()
                .centerInfo(centerInfo)
                .name(ticket.getName())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .totalCount(ticket.getTotalCount())
                .issueCount(ticket.getIssueCount())
                .build();
    }
}
