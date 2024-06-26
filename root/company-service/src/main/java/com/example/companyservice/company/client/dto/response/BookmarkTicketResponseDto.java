package com.example.companyservice.company.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkTicketResponseDto {

    private Long ticketId;

    private String name;

    private int calculatedPrice;

    private int duration;
}
