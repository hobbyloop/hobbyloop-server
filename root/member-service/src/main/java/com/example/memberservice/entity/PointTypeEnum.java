package com.example.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PointTypeEnum {
    USE(1),
    ACC(2);

    private final int typeValue;

    public static PointTypeEnum findByValue(final int typeValue) {
        return Arrays.stream(PointTypeEnum.values())
                .filter(e -> e.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 포인트 사용 타입 입니다."));
    }
}
