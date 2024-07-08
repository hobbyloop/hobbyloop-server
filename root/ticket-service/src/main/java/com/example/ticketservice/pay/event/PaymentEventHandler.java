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
import org.springframework.transaction.TransactionDefinition;
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

        if (payment.getCheckout().getCouponDiscountAmount() > 0L) {
            MemberCoupon coupon = memberCouponRepository.findById(payment.getCheckout().getMemberCouponId())
                    .orElseThrow();
            coupon.use();
            payment.markCouponUpdated();
        }

        payment.complete();

        paymentRepository.save(payment);
        checkoutRepository.save(checkout);

        // 결제한 사이에 이용권을 발급받을 수 없는 상황이 되면 바로 환불 진행
        Ticket ticket = ticketRepository.findById(payment.getTicket().getId()).orElseThrow();
        if (!ticket.canPurchase()) {
            transactionTemplate.execute(status -> {
                paymentService.refund(payment.getMemberId(), payment.getId());
                return null;
            });
            return;
        }

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.execute(status -> {
            UserTicket userTicket = UserTicket.of(ticket, payment.getMemberId());
            userTicketRepository.save(userTicket);
            payment.setUserTicket(userTicket);

            log.info("payment's user ticket has been saved" + payment.getUserTicket());
            return null;
        });

        transactionTemplate.execute(status -> {
            paymentRepository.save(payment);
            return null;
        });
    }

    // 전액환불의 경우 포인트, 쿠폰 전부 반환
    // 부분환불의 경우 포인트는 환급하나 쿠폰은 소멸
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentRefundedEvent(PaymentRefundedEvent event) {
        PaymentRefund refund = event.getRefund();
        Payment payment = refund.getPayment();

        // 사용한 포인트 반환
        pointService.restoreUsedPointWhenRefund(refund.getMemberId(), payment.getCheckout().getPointUsages(), payment.getTicket().getName());
        refund.markPointUpdated();

        // 결제로 적립된 포인트 차감
        pointService.revokeEarnedPointWhenRefund(payment);

        if (!refund.isPartialRefund()) {
            // 쿠폰 반환(전액환불일 시), 부분환불일 시 쿠폰 소멸
            MemberCoupon coupon = memberCouponRepository.findById(payment.getCheckout().getMemberCouponId())
                    .orElseThrow();
            coupon.restore();
        }
        refund.markCouponUpdated();

        refund.complete();
        if (refund.isRefundDone()) {
            payment.refund();
        }

        paymentRepository.save(payment);
        paymentRefundRepository.save(refund);
    }
}
