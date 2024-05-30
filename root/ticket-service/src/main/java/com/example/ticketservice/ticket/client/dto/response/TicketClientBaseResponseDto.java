package com.example.ticketservice.ticket.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketClientBaseResponseDto {

    private int membershipCount;

    private List<TicketClientResponseDto> ticketClientResponseDtoList;

    public static TicketClientBaseResponseDto of(int membershipCount, List<TicketClientResponseDto> ticketClientResponseDtoList) {
        return TicketClientBaseResponseDto.builder()
                .membershipCount(membershipCount)
                .ticketClientResponseDtoList(ticketClientResponseDtoList)
                .build();
    }
}
