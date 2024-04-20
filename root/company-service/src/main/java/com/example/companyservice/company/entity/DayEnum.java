package com.example.companyservice.company.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DayEnum {
    SUN(0, "일"),
    MON(1, "월"),
    TUE(2, "화"),
    WED(3, "수"),
    THU(4, "목"),
    FRI(5, "금"),
    SAT(6, "토");

    private final int typeValue;

    private final String dayName;

    public static DayEnum findByValue(final int typeValue) {
        return Arrays.stream(DayEnum.values())
                .filter(e -> e.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 요일 입니다."));
    }

    public static DayEnum findByName(final String name) {
        return Arrays.stream(DayEnum.values())
                .filter(e -> e.dayName.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 요일 입니다."));
    }
}
