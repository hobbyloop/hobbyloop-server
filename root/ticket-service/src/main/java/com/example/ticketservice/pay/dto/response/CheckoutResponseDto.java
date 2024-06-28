package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckoutResponseDto {
    private Long checkoutId;

    private Long paymentId;

    private String idempotencyKey;

    private int type;

    private int method;

    private Long finalAmount;

    public static CheckoutResponseDto of(Checkout checkout, Payment payment) {
        return CheckoutResponseDto.builder()
                .checkoutId(checkout.getId())
                .paymentId(payment.getId())
                .idempotencyKey(checkout.getIdempotencyKey())
                .type(checkout.getType())
                .method(checkout.getMethod())
                .finalAmount(checkout.getFinalAmount())
                .build();
    }
}
