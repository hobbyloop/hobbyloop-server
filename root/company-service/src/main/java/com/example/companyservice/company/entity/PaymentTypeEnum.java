package com.example.companyservice.company.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {

    POINT(1, "광고 포인트 차감"),
    PRE(2, "선결제"),
    POST(3, "후불결제");

    private final int typeValue;

    private final String name;

    public static PaymentTypeEnum findByValue(final int typeValue) {
        return Arrays.stream(PaymentTypeEnum.values())
                .filter(e -> e.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방법 입니다."));
    }

    public static PaymentTypeEnum findByName(final String name) {
        return Arrays.stream(PaymentTypeEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방법 입니다."));
    }
}
