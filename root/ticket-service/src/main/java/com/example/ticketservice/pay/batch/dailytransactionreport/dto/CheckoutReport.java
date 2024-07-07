package com.example.ticketservice.pay.batch.dailytransactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutReport {
    private int checkoutCount;
    private Long totalDiscountAmount;
}
