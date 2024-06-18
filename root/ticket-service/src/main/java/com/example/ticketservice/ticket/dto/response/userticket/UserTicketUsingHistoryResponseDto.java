package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.entity.UserTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTicketUsingHistoryResponseDto {
    private Long ticketId;
    private String ticketImageUrl;
    private String ticketName;
    private String centerName;
    private int remainingCount;
    private int totalCounting;
    private List<UsingHistoryByMonthDto> usingHistoryByMonth;

    public static UserTicketUsingHistoryResponseDto of(UserTicket userTicket, String centerName, List<UsingHistoryByMonthDto> usingHistoryByMonth) {
        return UserTicketUsingHistoryResponseDto.builder()
                .ticketId(userTicket.getTicket().getId())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .ticketName(userTicket.getTicket().getName())
                .centerName(centerName)
                .remainingCount(userTicket.getRemainingCount())
                .totalCounting(userTicket.getTicket().getUseCount())
                .usingHistoryByMonth(usingHistoryByMonth)
                .build();
    }
}
