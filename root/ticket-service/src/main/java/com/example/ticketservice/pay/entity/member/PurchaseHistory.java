package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PurchaseHistory extends TimeStamped {

    @Id
    @Column(name = "purchase_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentRefund refund;

    private int type;

    private LocalDateTime date;

    private Long amount;

    private Long usePoint;

    private boolean isRefund;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    private Long memberCouponId;

    private Long looppassId;

    private int previousStatus;

    private int newStatus;

    @Column(length = 1000)
    private String updateReason;

    // test
    public static PurchaseHistory of(Long memberId,
                                     Ticket ticket,
                                     Long memberCouponId) {
        return PurchaseHistory.builder()
                .type(1)
                .date(LocalDateTime.now())
                .amount(10000L)
                .usePoint(1000L)
                .isRefund(false)
                .memberId(memberId)
                .ticket(ticket)
                .memberCouponId(memberCouponId)
                .build();
    }

    public static PurchaseHistory record(Payment payment,
                                         PaymentStatusEnum previousStatus,
                                         PaymentStatusEnum newStatus,
                                         String updateReason) {
        return PurchaseHistory.builder()
                .payment(payment)
                .type(1)
                .amount(payment.getAmount())
                .isRefund(false)
                .memberId(payment.getMemberId())
                .ticket(payment.getTicket())
                .previousStatus(previousStatus.getValue())
                .newStatus(newStatus.getValue())
                .updateReason(updateReason)
                .build();
    }

    public static PurchaseHistory record(PaymentRefund refund,
                                         PaymentStatusEnum previousStatus,
                                         PaymentStatusEnum newStatus,
                                         String updateReason) {
        return PurchaseHistory.builder()
                .refund(refund)
                .type(1)
                .amount(refund.getAmount())
                .isRefund(true)
                .memberId(refund.getMemberId())
                .ticket(refund.getPayment().getTicket())
                .previousStatus(previousStatus.getValue())
                .newStatus(newStatus.getValue())
                .updateReason(updateReason)
                .build();
    }
}
