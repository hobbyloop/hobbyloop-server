package com.example.ticketservice.pay.batch.dailytransactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentStepMetrics {
    private Integer totalTransactionCount;
    private Long netSales;
    private Integer paymentFailedCount;
    private Integer paymentAttemptCount;
    private Integer paymentSuccessCount;
    private Integer transactionHour;
    private Long memberId;
}
