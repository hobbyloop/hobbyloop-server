package com.example.ticketservice.ticket.dto.response.userticket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsingHistoryByMonthDto {
    @Schema(description = "이용권을 사용한 연월(이 필드 기준으로 내림차순 정렬)", example = "2024/06")
    private String yearMonth;

    @Schema(description = "해당 연월의 이용권 사용 내역")
    private List<UsingHistoryDto> usingHistories;
}
