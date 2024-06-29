package com.example.ticketservice.pay.entity.member.enums;

import com.example.ticketservice.pay.entity.CompanyPaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {
    NOT_STARTED(1),
    EXECUTING(2),
    SUCCESS(3),
    FAILURE(4),
    UNKNOWN(5);

    private int value;

    public static PaymentStatusEnum findByValue(final int status) {
        return Arrays.stream(PaymentStatusEnum.values())
                .filter(e -> e.value == status)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방식 입니다."));
    }
}
