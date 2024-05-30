package com.example.ticketservice.ticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableUserTicketsWithCenterInfo {
    private Long centerId;
    private boolean refundable;
    private List<AvailableUserTicketResponseDto> availableUserTickets;

    public static AvailableUserTicketsWithCenterInfo of(Long centerId, boolean isRefundable, List<AvailableUserTicketResponseDto> availableUserTickets) {
        return AvailableUserTicketsWithCenterInfo.builder()
                .centerId(centerId)
                .refundable(isRefundable)
                .availableUserTickets(availableUserTickets)
                .build();
    }
}
