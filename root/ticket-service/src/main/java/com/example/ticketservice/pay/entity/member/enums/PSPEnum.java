package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PSPEnum {
    TOSS(1);

    private final int value;

    public static PSPEnum findByValue(final int value) {
        return Arrays.stream(PSPEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 결제 방식입니다."));
    }
}
