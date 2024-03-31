package com.example.instructorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ReservationStatusEnum {

    CONFIRM(1),
    WAITING(2),
    REJECT(3);

    private final int reservationStatusType;

    public static ReservationStatusEnum findByValue(final int reservationStatusType) {
        return Arrays.stream(ReservationStatusEnum.values())
                .filter(e -> e.reservationStatusType == reservationStatusType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 예약 상태 입니다."));
    }
}
