package com.example.ticketservice.pay.event;

import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventHandler {
    private final PointService pointService;

    @TransactionalEventListener
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        Payment payment = event.getPayment();

        pointService.usePointWhenPurchase(payment.getMemberId(), payment.getCheckout().getPointUsages());
        payment.markPointUpdated();
    }
}
