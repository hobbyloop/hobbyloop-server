package com.example.ticketservice.pay;

import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.entity.CouponDiscountTypeEnum;
import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.dto.response.TossPaymentConfirmResponseDto;
import com.example.ticketservice.pay.entity.member.enums.PSPConfirmationStatusEnum;

import java.time.LocalDateTime;

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

    public static PaymentConfirmRequestDto defaultPaymentConfirmRequest(CheckoutResponseDto response) {
        return new PaymentConfirmRequestDto(
                response.getPaymentId(),
                "randomString",
                response.getIdempotencyKey(),
                response.getFinalAmount(),
                1
        );
    }

    public static PaymentConfirmExecuteResponseDto defaultPaymentConfirmExecuteSuccessResponse(CheckoutResponseDto response) {
        return PaymentConfirmExecuteResponseDto.builder()
                .paymentKey("randomString")
                .IdempotencyKey(response.getIdempotencyKey())
                .type("일반결제")
                .method("간편결제")
                .approvedAt(LocalDateTime.now())
                .pspConfirmationStatus(PSPConfirmationStatusEnum.DONE.getValue())
                .totalAmount(response.getFinalAmount())
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }

}
