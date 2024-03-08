package com.example.payservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EntryAuthorityEnum {

    COMMON(1, "일반"),
    ONEDAY(2, "원데이&소수클래스"),
    PREMIUM(3, "프리미엄");

    private final int entryAuthorityType;

    private final String name;

    public static EntryAuthorityEnum findByValue(final int entryAuthorityType) {
        return Arrays.stream(EntryAuthorityEnum.values())
                .filter(e -> e.entryAuthorityType == entryAuthorityType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 입점 권한 입니다."));
    }

    public static EntryAuthorityEnum findByName(final String name) {
        return Arrays.stream(EntryAuthorityEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 입점 권한 입니다."));
    }
}
