package com.example.ticketservice.ticket.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OriginalBusinessResponseDto {

    private String representativeName;

    private String businessNumber;

    private LocalDate openingDate;

    private String onlineReportNumber;
}
