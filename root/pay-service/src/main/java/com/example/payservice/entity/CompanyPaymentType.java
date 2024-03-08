package com.example.payservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CompanyPaymentType {

    FIX(1, "정률제"), // 정률제
    FLAT(2, "정액제"); // 정액제

    private final int paymentType;

    private final String name;

    public static CompanyPaymentType findByValue(final int paymentType) {
        return Arrays.stream(CompanyPaymentType.values())
                .filter(e -> e.paymentType == paymentType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방식 입니다."));
    }

    public static CompanyPaymentType findByName(final String name) {
        return Arrays.stream(CompanyPaymentType.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방식 입니다."));
    }
}
