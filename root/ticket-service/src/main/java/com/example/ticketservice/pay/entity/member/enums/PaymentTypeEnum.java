package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {
    ONE_TIME(1, "일반결제"),
    AUTO_BILLING(2, "자동결제");

    private int value;
    private String description;

    public static PaymentTypeEnum findByValue(final int value) {
        return Arrays.stream(PaymentTypeEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 유형입니다."));
    }
}
