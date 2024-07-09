package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TransactionErrorStatusEnum {
    OPEN(1),
    IN_PROGRESS(2),
    RESOLVED(3),
    IGNORED(4);

    private final int value;

    public static TransactionErrorStatusEnum findByValue(final int value) {
        return Arrays.stream(TransactionErrorStatusEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 에러입니다."));
    }
}
