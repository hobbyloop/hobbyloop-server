package com.example.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GenderType {

    MAN(1),
    WOMAN(2);

    private final int genderType;

    public static GenderType findByValue(final int genderType) {
        return Arrays.stream(GenderType.values())
                .filter(e -> e.genderType == genderType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 성별 입니다."));
    }
}
