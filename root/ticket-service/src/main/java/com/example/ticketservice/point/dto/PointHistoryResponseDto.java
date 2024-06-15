package com.example.ticketservice.point.dto;

import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointHistoryResponseDto {
    private String type;
    private Long amount;
    private Long balance;
    private String description;
    private LocalDateTime createdAt;

    public static PointHistoryResponseDto from(PointHistory pointHistory) {
        return PointHistoryResponseDto.builder()
                .type(PointTypeEnum.findByValue(pointHistory.getType()).name())
                .amount(pointHistory.getAmount())
                .balance(pointHistory.getBalance())
                .description(pointHistory.getDescription())
                .createdAt(pointHistory.getCreatedAt())
                .build();
    }
}
