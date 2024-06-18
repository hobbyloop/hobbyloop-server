package com.example.ticketservice.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CouponDiscountTypeEnum {
    AMOUNT(1),
    PERCENTAGE(2);

    private final int value;

    public static CouponDiscountTypeEnum findByValue(final int value) {
        return Arrays.stream(CouponDiscountTypeEnum.values())
                .filter(e -> e.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("잘못된... 어쩌구"));
    }
}
