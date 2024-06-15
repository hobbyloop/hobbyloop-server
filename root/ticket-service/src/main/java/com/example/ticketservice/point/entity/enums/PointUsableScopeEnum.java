package com.example.ticketservice.point.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PointUsableScopeEnum {
    GENERAL(1),
    SPECIFIC_COMPANY(2),
    SPECIFIC_CENTER(3);

    private final int value;

    public static PointUsableScopeEnum findByValue(int value) {
        return Arrays.stream(PointUsableScopeEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow();
    }
}
