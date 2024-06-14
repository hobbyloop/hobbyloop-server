package com.example.ticketservice.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PointHistoryListResponseDto {
    private Long totalPoints;
    private List<PointHistoryByMonthResponseDto> pointHistories;
}
