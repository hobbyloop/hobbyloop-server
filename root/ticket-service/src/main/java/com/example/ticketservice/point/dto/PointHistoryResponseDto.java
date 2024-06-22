package com.example.ticketservice.point.dto;

import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryResponseDto {
    @Schema(description = "포인트 유형(EARN, USE)", example = "EARN")
    private String type;

    @Schema(description = "적립/사용 금액", example = "2500")
    private Long amount;

    @Schema(description = "적립/사용 시의 잔액", example = "4200")
    private Long balance;

    @Schema(description = "설명", example = "하비루프 스튜디오 이용권 사용")
    private String description;

    @Schema(description = "적립/사용 시점")
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
