package com.example.companyservice.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RequestStatus {
    WAIT(0, "대기"),
    ALLOW(1, "승인"),
    REJECT(2, "거절");

    private final int typeValue;

    private final String name;

    public static RequestStatus findByValue(final int typeValue) {
        return Arrays.stream(RequestStatus.values())
                .filter(r -> r.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 승인 상태 입니다."));
    }

    public static RequestStatus findByName(final String name) {
        return Arrays.stream(RequestStatus.values())
                .filter(r -> r.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 승인 상태 입니다."));
    }
}
