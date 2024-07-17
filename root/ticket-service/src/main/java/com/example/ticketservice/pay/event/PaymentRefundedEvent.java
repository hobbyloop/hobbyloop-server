package com.example.ticketservice.pay.event;

import com.example.ticketservice.pay.entity.member.PaymentRefund;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRefundedEvent {
    private final PaymentRefund refund;
}
