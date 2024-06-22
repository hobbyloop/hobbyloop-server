package com.example.ticketservice.ticket.client.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "대표명", example = "홍길동")
    private String representativeName;

    @Schema(description = "사업자번호")
    private String businessNumber;

    @Schema(description = "개업일자", example = "2024-06-29")
    private LocalDate openingDate;

    @Schema(description = "통신판매번호")
    private String onlineReportNumber;
}
