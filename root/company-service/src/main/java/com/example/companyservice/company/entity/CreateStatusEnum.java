package com.example.companyservice.company.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CreateStatusEnum {

    NOT(1, "신청안함"),
    WAIT(2, "대기"),
    SUCCESS(3, "완료"),
    REJECT(4, "실패");

    private final int typeValue;

    private final String name;

    public static CreateStatusEnum findByValue(final int typeValue) {
        return Arrays.stream(CreateStatusEnum.values())
                .filter(e -> e.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 승인 상태 입니다."));
    }

    public static CreateStatusEnum findByName(final String name) {
        return Arrays.stream(CreateStatusEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 승인 상태 입니다."));
    }
}
