package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReviewTicketResponseDto {

    private String centerName;

    private String address;

    private String ticketName;

    private float score;

    public static AdminReviewTicketResponseDto of(CenterInfoResponseDto responseDto,
                                                  Ticket ticket,
                                                  float score) {
        return AdminReviewTicketResponseDto.builder()
                .centerName(responseDto.getCenterName())
                .address(responseDto.getAddress())
                .ticketName(ticket.getName())
                .score(score)
                .build();
    }
}
