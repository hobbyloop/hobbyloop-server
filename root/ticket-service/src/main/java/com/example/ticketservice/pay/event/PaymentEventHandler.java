package com.example.ticketservice.pay.event;

import com.example.ticketservice.coupon.entity.MemberCoupon;
import com.example.ticketservice.coupon.repository.MemberCouponRepository;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventHandler {
    private final PointService pointService;
    private final MemberCouponRepository memberCouponRepository;

    @TransactionalEventListener
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        Payment payment = event.getPayment();

        pointService.usePointWhenPurchase(payment.getMemberId(), payment.getCheckout().getPointUsages(), payment.getTicket().getName());
        payment.markPointUpdated();

        // TODO: companyId
        pointService.earnPointWhenPurchase(payment.getMemberId(), payment.getTicket().getCompanyId(), payment.getTicket().getCenterId(), payment.getAmount());

        MemberCoupon coupon = memberCouponRepository.findById(payment.getCheckout().getMemberCouponId())
                .orElseThrow();
        coupon.use();
        payment.markCouponUpdated();
    }
}
