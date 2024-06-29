package com.example.ticketservice.pay.exception;

import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentAlreadyProcessedException extends RuntimeException {
    private final PaymentStatusEnum status;
    private final String message;
}
