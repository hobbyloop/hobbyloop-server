package com.example.companyservice.instructor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum InstructorAuthEnum {
    ADMIN(0, "관리자"),
    HEAD(1, "실장"),
    FULLTIME(2, "전임강사"),
    PARTTIME(3, "파트타임강사");

    private final int value;

    private final String name;

    public static InstructorAuthEnum findByValue(final int value) {
        return Arrays.stream(InstructorAuthEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 강사 권한 입니다."));
    }

    public static InstructorAuthEnum findByName(final String name) {
        return Arrays.stream(InstructorAuthEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 강사 권한 입니다."));
    }
}
