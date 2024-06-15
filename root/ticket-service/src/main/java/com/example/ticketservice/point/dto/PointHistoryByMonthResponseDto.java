package com.example.ticketservice.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PointHistoryByMonthResponseDto {
    private String yearMonth;
    private List<PointHistoryResponseDto> pointHistories;
}
