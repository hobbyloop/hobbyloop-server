package com.example.companyservice.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotTicketResponseDto {

    private Long ticketId;

    private String ticketName;

    private float score;

    private int reviewCount;

    private String category;

    private int price;
}
