package com.example.ticketservice.pay.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentDetailResponseDto {
    private String buyerName;
    private String phoneNumber;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private Long amount;
    private String couponName;
    private Long couponDiscountAmount;
    private Long point;
    private String ticketName;
    private int remainingCount;
}
