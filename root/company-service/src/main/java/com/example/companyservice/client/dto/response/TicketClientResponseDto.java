package com.example.companyservice.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketClientResponseDto {

    private Long ticketId;

    private String name;

    private int calculatedPrice;

    private int duration;
}
