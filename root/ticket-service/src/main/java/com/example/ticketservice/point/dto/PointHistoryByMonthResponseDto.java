package com.example.ticketservice.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryByMonthResponseDto {
    @Schema(description = "연월", example = "2024/06")
    private String yearMonth;

    @Schema(description = "포인트 내역")
    private List<PointHistoryResponseDto> pointHistories;
}
