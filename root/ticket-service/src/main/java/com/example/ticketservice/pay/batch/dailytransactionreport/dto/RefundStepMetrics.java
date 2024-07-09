package com.example.ticketservice.pay.batch.dailytransactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefundStepMetrics {
    private Long refundAmount;
    private Integer refundCount;
}
