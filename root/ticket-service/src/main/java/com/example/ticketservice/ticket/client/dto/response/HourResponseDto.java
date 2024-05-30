package com.example.ticketservice.ticket.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HourResponseDto {

    private String day;

    private LocalTime openAt;

    private LocalTime closeAt;
}
