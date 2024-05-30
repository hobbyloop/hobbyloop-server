package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.UserTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentPurchaseUserTicketListResponseDto {
    private Long userTicketId;
    private String ticketImageUrl;
    private String ticketName;
    private String centerName;
    private LocalDateTime createdAt;

    public static RecentPurchaseUserTicketListResponseDto of(UserTicket userTicket, String centerName) {
        return RecentPurchaseUserTicketListResponseDto.builder()
                .userTicketId(userTicket.getId())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .ticketName(userTicket.getTicket().getName())
                .centerName(centerName)
                .createdAt(userTicket.getCreatedAt())
                .build();
    }
}
