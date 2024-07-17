package com.example.ticketservice.pay.batch.dailytransactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutStepMetrics {
    private int checkoutCount;
    private Long totalDiscountAmount;
}
