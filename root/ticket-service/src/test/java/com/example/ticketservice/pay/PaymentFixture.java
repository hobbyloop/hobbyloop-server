package com.example.ticketservice.pay;

import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.entity.CouponDiscountTypeEnum;
import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;

public class PaymentFixture {

    public static CheckoutRequestDto defaultCheckoutRequest(CheckoutPrepareResponseDto response) {
        MemberCouponResponseDto coupon = response.getUsableCoupons().get(0);
        Long couponDiscountAmount;
        if (coupon.getDiscountType().equals(CouponDiscountTypeEnum.AMOUNT.name())) {
            couponDiscountAmount = coupon.getDiscountAmount();
        } else {
            couponDiscountAmount = response.getOriginalAmount() * coupon.getDiscountPercentage() / 100;
        }
        Long totalDiscountAmount = couponDiscountAmount + response.getMaxUsablePoints();
        Long finalAmount = response.getOriginalAmount() - totalDiscountAmount;

        return CheckoutRequestDto.builder()
                .checkoutId(response.getCheckoutId())
                .idempotencyKey(response.getIdempotencyKey())
                .type(1)
                .method(1)
                .memberCouponId(response.getUsableCoupons().get(0).getMemberCouponId())
                .points(response.getMaxUsablePoints())
                .couponDiscountAmount(couponDiscountAmount)
                .totalDiscountAmount(totalDiscountAmount)
                .finalAmount(finalAmount)
                .build();
    }
}
