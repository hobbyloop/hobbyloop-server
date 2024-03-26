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

    private Long ticketId;

    private CenterInfoResponseDto centerInfo;

    private String name;

    private String ticketImageUrl;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    private int totalCount;

    private int issueCount;

    private boolean isUpload;

    private float score;

    public static AdminTicketResponseDto of(CenterInfoResponseDto centerInfo, Ticket ticket, float score) {
        return AdminTicketResponseDto.builder()
                .ticketId(ticket.getId())
                .centerInfo(centerInfo)
                .name(ticket.getName())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .totalCount(ticket.getTotalCount())
                .issueCount(ticket.getIssueCount())
                .isUpload(ticket.isUpload())
                .score(score)
                .build();
    }
}
