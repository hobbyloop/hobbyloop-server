package com.example.companyservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuthorityEnum {

    관리자(1),
    실장(2),
    전임강사(3),
    하프타임강사(4);

    private final int authorityValue;

    public static AuthorityEnum findByValue(final int AuthorityValue) {
        return Arrays.stream(AuthorityEnum.values())
                .filter(e -> e.authorityValue == AuthorityValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 권한 입니다."));
    }
}
