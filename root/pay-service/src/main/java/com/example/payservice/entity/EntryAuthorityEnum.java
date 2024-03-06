package com.example.payservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EntryAuthorityEnum {

    COMMON(1),
    ONEDAY(2),
    PREMIUM(3);

    private final int entryAuthorityType;

    public static EntryAuthorityEnum findByValue(final int entryAuthorityType) {
        return Arrays.stream(EntryAuthorityEnum.values())
                .filter(e -> e.entryAuthorityType == entryAuthorityType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 입점 권한 입니다."));
    }
}
