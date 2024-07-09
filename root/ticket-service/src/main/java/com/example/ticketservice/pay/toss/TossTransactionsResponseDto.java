package com.example.ticketservice.pay.toss;

import lombok.Getter;

@Getter
public class TossTransactionsResponseDto {
    private String mId;
    private String transactionKey;
    private String paymentKey;
    private String orderId;
    private String method;
    private String customerKey;
    private Boolean useEscrow;
    private String receiptUrl;
    private String status;
    private String transactionAt;
    private String currency;
    private Integer amount;
}
