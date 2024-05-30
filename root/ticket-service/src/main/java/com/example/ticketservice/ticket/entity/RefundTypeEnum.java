package com.example.ticketservice.ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RefundTypeEnum {

    REFUND1(1),
    REFUND2(2),
    REFUND3(3),
    REFUND4(4),
    REFUND5(5);

    private final int refundType;

    public static RefundTypeEnum findByValue(final int refundType) {
        return Arrays.stream(RefundTypeEnum.values())
                .filter(e -> e.refundType == refundType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 환불 규정 입니다."));
    }
}
