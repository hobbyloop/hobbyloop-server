package com.example.companyservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDto {

    private Long ticketId;

    private String name;

    private int price;

    private int duration;
}
