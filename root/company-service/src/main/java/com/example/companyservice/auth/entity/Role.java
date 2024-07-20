package com.example.companyservice.auth.entity;

import com.example.companyservice.company.entity.DayEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    COMPANY("COMPANY"),
    USER("USER"),
    INSTRUCTOR("INSTRUCTOR"),
    ADMIN("ADMIN");

    private final String name;

    public static Role findByName(final String name) {
        return Arrays.stream(Role.values())
                .filter(r -> r.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 권한 입니다."));
    }
}
