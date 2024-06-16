package com.example.ticketservice.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryListResponseDto {
    private Long totalPoints;
    private List<PointHistoryByMonthResponseDto> pointHistories;
}
