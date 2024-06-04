package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.LectureReservation;
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
    private int useCount;
    private int remainingCount;
    private LocalDateTime usedAt;

    public static UsingHistoryDto from(LectureReservation reservation) {
        return UsingHistoryDto.builder()
                .useCount(1)
                .remainingCount(reservation.getRemainingCount())
                .usedAt(reservation.getCreatedAt())
                .build();
    }
}
