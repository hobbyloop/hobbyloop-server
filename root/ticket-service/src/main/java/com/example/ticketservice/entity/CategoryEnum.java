package com.example.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CategoryEnum {

    HEALTH(1, "헬스/피티"), // 헬스/피티
    PILATES(2, "필라테스"), // 필라테스
    WORKSHOP(3, "공방"), // 공방
    COOKING(4, "쿠킹"), // 쿠킹
    PAINTING(5, "그림"), // 그림
    PET(6, "반려동물"); // 반려동물

    private final int categoryType;

    private final String name;

    public static CategoryEnum findByValue(final int categoryType) {
        return Arrays.stream(CategoryEnum.values())
                .filter(e -> e.categoryType == categoryType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 카테고리 입니다."));
    }

    public static CategoryEnum findByName(final String name) {
        return Arrays.stream(CategoryEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 카테고리 입니다."));
    }
}
