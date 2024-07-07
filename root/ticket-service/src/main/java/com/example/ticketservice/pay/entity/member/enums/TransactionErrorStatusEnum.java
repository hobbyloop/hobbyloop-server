package com.example.ticketservice.pay.entity.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionErrorStatusEnum {
    OPEN(1),
    IN_PROGRESS(2),
    RESOLVED(3),
    IGNORED(4);

    private final int value;
}
