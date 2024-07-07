package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionErrorTypeEnum {
    AMOUNT_MISMATCH(1),
    TRANSACTION_NOT_FOUND(2),
    PAYMENT_NOT_FOUND(3),
    REFUND_NOT_FOUND(4),
    STATUS_MISMATCH(5);

    private final int value;
}
