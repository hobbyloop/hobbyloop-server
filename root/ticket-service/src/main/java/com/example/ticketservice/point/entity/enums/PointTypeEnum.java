package com.example.ticketservice.point.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PointTypeEnum {
    EARN(1),
    USE(2),
    EXPIRE(3);

    private final int value;

    public static PointTypeEnum findByValue(int value) {
        return Arrays.stream(PointTypeEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow();
    }
}
