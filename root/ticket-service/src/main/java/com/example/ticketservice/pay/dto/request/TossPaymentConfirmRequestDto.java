package com.example.ticketservice.pay.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossPaymentConfirmRequestDto {
    private String paymentKey;
    private String orderId;
    private Long amount;

    public static TossPaymentConfirmRequestDto from(PaymentConfirmRequestDto requestDto) {
        return TossPaymentConfirmRequestDto.builder()
                .paymentKey(requestDto.getPaymentKey())
                .orderId(requestDto.getIdempotencyKey())
                .amount(requestDto.getAmount())
                .build();
    }
}
