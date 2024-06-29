package com.example.ticketservice.pay.event;

import com.example.ticketservice.pay.entity.member.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCompletedEvent {
    private final Payment payment;
}
