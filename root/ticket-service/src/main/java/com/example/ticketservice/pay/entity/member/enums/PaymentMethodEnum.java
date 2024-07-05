package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentMethodEnum {
    CARD(1),
    VIRTUAL_ACCOUNT(2),
    TRANSFER(3),
    EASY_PAY(4);

    private int value;

    public static PaymentMethodEnum findByValue(final int value) {
        return Arrays.stream(PaymentMethodEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방식입니다."));
    }
}
