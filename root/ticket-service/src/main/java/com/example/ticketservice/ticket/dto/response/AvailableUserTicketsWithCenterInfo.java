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
    private String centerName;
    private Long centerId;
    private boolean refundable;
    private List<AvailableUserTicketResponseDto> availableUserTickets;

    public static AvailableUserTicketsWithCenterInfo of(Long centerId, String centerName, boolean isRefundable, List<AvailableUserTicketResponseDto> availableUserTickets) {
        return AvailableUserTicketsWithCenterInfo.builder()
                .centerId(centerId)
                .centerName(centerName)
                .refundable(isRefundable)
                .availableUserTickets(availableUserTickets)
                .build();
    }
}
