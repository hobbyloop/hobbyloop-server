package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.UserTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTicketExpiringHistoryResponseDto {
    private Long ticketId;
    private String ticketImageUrl;
    private String ticketName;
    private String centerName;
    private int remainingCount;
    private int totalCounting;
    private String yearMonth;
    private int expireCount;
    private LocalDate expiredAt;

    public static UserTicketExpiringHistoryResponseDto of(UserTicket userTicket, String centerName, String yearMonth) {
        return UserTicketExpiringHistoryResponseDto.builder()
                .ticketId(userTicket.getId())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .ticketName(userTicket.getTicket().getName())
                .centerName(centerName)
                .remainingCount(0)
                .totalCounting(userTicket.getTicket().getUseCount())
                .yearMonth(yearMonth)
                .expireCount(userTicket.getRemainingCount())
                .expiredAt(userTicket.getEndDate())
                .build();
    }
}
