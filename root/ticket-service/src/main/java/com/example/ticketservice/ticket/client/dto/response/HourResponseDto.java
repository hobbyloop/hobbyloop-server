package com.example.ticketservice.ticket.client.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "요일", example = "토")
    private String day;

    @Schema(description = "오픈시간", example = "09:00")
    private LocalTime openAt;

    @Schema(description = "종료시간", example = "21:00")
    private LocalTime closeAt;
}
