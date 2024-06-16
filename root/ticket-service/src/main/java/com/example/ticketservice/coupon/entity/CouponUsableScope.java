package com.example.ticketservice.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponUsableScope {
    GENERAL(1),
    SPECIFIC_COMPANY(2),
    SPECIFIC_CENTER(3);

    private final int value;
}
