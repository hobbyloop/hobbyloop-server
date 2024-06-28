package com.example.ticketservice.pay.dto.request;

import lombok.Getter;

@Getter
public class CheckoutRequestDto {
    private Long checkoutId;

    private String idempotencyKey;

    private int type;

    private int method;

    private Long memberCouponId;

    private Long points;

    private Long totalDiscountAmount;

    private Long finalAmount;

    private Long couponDiscountAmount;
}
