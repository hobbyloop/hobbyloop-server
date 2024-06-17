package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.entity.UserTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableUserTicketResponseDto {
    private long userTicketId;
    private String ticketName;
    private String ticketImageUrl;
    private LocalDate startDate;
    private LocalDate endDate;

    public static AvailableUserTicketResponseDto of(UserTicket userTicket) {
        return AvailableUserTicketResponseDto.builder()
                .userTicketId(userTicket.getId())
                .ticketName(userTicket.getTicket().getName())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .startDate(userTicket.getStartDate())
                .endDate(userTicket.getEndDate())
                .build();
    }
}
