package com.example.ticketservice.pay.event;

import com.example.ticketservice.coupon.entity.MemberCoupon;
import com.example.ticketservice.coupon.repository.MemberCouponRepository;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PaymentRefund;
import com.example.ticketservice.pay.entity.member.vo.PointUsage;
import com.example.ticketservice.pay.repository.CheckoutRepository;
import com.example.ticketservice.pay.repository.PaymentRefundRepository;
import com.example.ticketservice.pay.repository.payment.PaymentRepository;
import com.example.ticketservice.pay.service.PaymentService;
import com.example.ticketservice.point.service.PointService;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandler {
    private final TransactionTemplate transactionTemplate;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final PaymentRefundRepository paymentRefundRepository;
    private final CheckoutRepository checkoutRepository;
    private final PointService pointService;
    private final MemberCouponRepository memberCouponRepository;
    private final TicketRepository ticketRepository;
    private final UserTicketRepository userTicketRepository;

    // TODO: TransactionTemplate으로 트랜잭션 쪼개기
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        Payment payment = event.getPayment();
        Checkout checkout = payment.getCheckout();

        List<PointUsage> pointUsages = pointService.usePointWhenPurchase(payment.getMemberId(), payment.getCheckout().getPointUsages(), payment.getTicket().getName());
        checkout.setPointUsages(pointUsages);
        payment.markPointUpdated();

        // TODO: companyId
        pointService.earnPointWhenPurchase(payment.getMemberId(), payment.getTicket().getCompanyId(), payment.getTicket().getCenterId(), payment.getAmount(), payment);

        MemberCoupon coupon = memberCouponRepository.findById(payment.getCheckout().getMemberCouponId())
                .orElseThrow();
        coupon.use();
        payment.markCouponUpdated();

        payment.complete();

        paymentRepository.save(payment);
        checkoutRepository.save(checkout);

        Ticket ticket = ticketRepository.findById(payment.getTicket().getId()).orElseThrow();
        if (!ticket.canPurchase()) {
            transactionTemplate.execute(status -> {
                paymentService.refund(payment.getMemberId(), payment.getId());
                return null;
            });
        }

        UserTicket userTicket = UserTicket.of(ticket, payment.getMemberId());
        userTicketRepository.save(userTicket);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentRefundedEvent(PaymentRefundedEvent event) {
        PaymentRefund refund = event.getRefund();
        Payment payment = refund.getPayment();

        pointService.restoreUsedPointWhenRefund(refund.getMemberId(), payment.getCheckout().getPointUsages(), payment.getTicket().getName());
        refund.markPointUpdated();

        // 결제로 적립된 포인트 차감
        pointService.revokeEarnedPointWhenRefund(payment);

        // 쿠폰 반환
        MemberCoupon coupon = memberCouponRepository.findById(payment.getCheckout().getMemberCouponId())
                .orElseThrow();
        coupon.restore();
        refund.markCouponUpdated();

        refund.complete();
        payment.refund();

        paymentRepository.save(payment);
        paymentRefundRepository.save(refund);
    }
}
