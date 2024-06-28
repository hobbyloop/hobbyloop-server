package com.example.ticketservice.pay.dto.request;

import lombok.Getter;

@Getter
public class PaymentConfirmRequestDto {
    private Long paymentId;
    private String paymentKey;
    private String idempotencyKey;
    private Long amount;
    private int psp;
}
