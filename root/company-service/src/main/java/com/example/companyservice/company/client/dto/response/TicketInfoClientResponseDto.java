package com.example.companyservice.company.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketInfoClientResponseDto {

    private Long ticketId;

    private String ticketName;

    private float score;

    private int reviewCount;

    private String category;

    private int calculatedPrice;
}
