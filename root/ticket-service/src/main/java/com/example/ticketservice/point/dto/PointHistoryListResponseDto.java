package com.example.ticketservice.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryListResponseDto {
    @Schema(description = "현재의 총 포인트 잔액", example = "50000")
    private Long totalPoints;

    @Schema(description = "포인트 내역 월별 그룹핑")
    private List<PointHistoryByMonthResponseDto> pointHistories;
}
