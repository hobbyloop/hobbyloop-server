package com.example.companyservice.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetailClientResponseDto {

    private float score;

    private int reviewCount;

    private int price;

    private int discountRate;

    private int calculatedPrice;
}
