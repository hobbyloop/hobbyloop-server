package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.entity.LectureReservation;
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
public class UsingHistoryDto {
    @Schema(description = "사용 횟수", example = "1")
    private int useCount;

    @Schema(description = "해당 시점의 남은 사용가능 횟수", example = "13")
    private int remainingCount;

    @Schema(description = "사용 시점(이 필드 기준으로 내림차순 정렬)", example = "2024-06-01T15:59:18.364433")
    private LocalDateTime usedAt;

    public static UsingHistoryDto from(LectureReservation reservation) {
        return UsingHistoryDto.builder()
                .useCount(1)
                .remainingCount(reservation.getRemainingCount())
                .usedAt(reservation.getCreatedAt())
                .build();
    }
}
