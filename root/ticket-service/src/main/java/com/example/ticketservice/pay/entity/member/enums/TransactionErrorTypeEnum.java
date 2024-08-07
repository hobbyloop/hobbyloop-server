package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TransactionErrorTypeEnum {
    AMOUNT_MISMATCH(1),
    TRANSACTION_NOT_FOUND(2),
    PAYMENT_NOT_FOUND(3),
    REFUND_NOT_FOUND(4),
    STATUS_MISMATCH(5);

    private final int value;

    public static TransactionErrorTypeEnum findByValue(final int value) {
        return Arrays.stream(TransactionErrorTypeEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 에러 유형입니다."));
    }
}
