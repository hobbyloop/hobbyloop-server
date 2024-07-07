package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.exception.PaymentAlreadyProcessedException;
import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Payment extends TimeStamped {
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Checkout checkout;

    private String idempotencyKey;

    private Long centerId; // sellerId

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    private Long looppassId; // TODO: Looppass로 바꾸기

    private Long amount;

    private int psp;

    private String pspPaymentKey;

    @Column(length = 1000)
    private String pspRawData;

    private int status; // not_started, executing, success

    private boolean isRefunded;

    private boolean isLedgerUpdated;

    private boolean isWalletUpdated;

    private Boolean isPointUpdated;

    private Boolean isCouponUpdated;

    private int failedCount;

    @Builder.Default
    private int threshold = 3;

    private String buyerName;

    private String phoneNumber;

    public static Payment checkout(Checkout checkout, Ticket ticket, MemberInfoResponseDto memberInfo) {
        Boolean isPointUpdated = null;
        Boolean isCouponUpdated = null;
        if (checkout.getPointDiscountAmount() > 0)
            isPointUpdated = false;
        if (checkout.getCouponDiscountAmount() > 0)
            isCouponUpdated = false;

        return Payment.builder()
                .checkout(checkout)
                .memberId(checkout.getMemberId())
                .idempotencyKey(checkout.getIdempotencyKey())
                .centerId(ticket.getCenterId())
                .ticket(ticket)
                .amount(checkout.getFinalAmount())
                .status(PaymentStatusEnum.NOT_STARTED.getValue())
                .isLedgerUpdated(false)
                .isWalletUpdated(false)
                .isPointUpdated(isPointUpdated)
                .isCouponUpdated(isCouponUpdated)
                .failedCount(0)
                .buyerName(memberInfo.getMemberName())
                .phoneNumber(memberInfo.getPhoneNumber())
                .build();
    }

    public void confirm(PaymentConfirmExecuteResponseDto responseDto) {
        this.status = PaymentStatusEnum.SUCCESS.getValue();
        this.pspRawData = responseDto.getPspRawData();
    }

    public void confirm() {
        this.status = PaymentStatusEnum.SUCCESS.getValue();
    }

    public void failOrUnknown(PaymentStatusEnum status, String errorCode, String errorMessage) {
        if (status == PaymentStatusEnum.FAILURE) {
            this.status = PaymentStatusEnum.FAILURE.getValue();
        } else if (status == PaymentStatusEnum.UNKNOWN) {
            this.status = PaymentStatusEnum.UNKNOWN.getValue();
            failedCount++;
        }

        this.pspRawData = errorCode + "_" + errorMessage;
    }

    public void execute(int psp, String pspPaymentKey) {
        if (this.status == PaymentStatusEnum.SUCCESS.getValue()) {
            throw new PaymentAlreadyProcessedException(PaymentStatusEnum.SUCCESS, "이미 성공한 결제입니다.");
        } else if (this.status == PaymentStatusEnum.FAILURE.getValue()) {
            throw new PaymentAlreadyProcessedException(PaymentStatusEnum.FAILURE, "이미 실패한 결제입니다.");
        }

        this.psp = psp;
        this.pspPaymentKey = pspPaymentKey;
        this.status = PaymentStatusEnum.EXECUTING.getValue();
    }

    public void markPointUpdated() {
        this.isPointUpdated = true;
    }

    public void markCouponUpdated() {
        this.isCouponUpdated = true;
    }

    public void complete() {
        if (isPointUpdated && isCouponUpdated) {
            checkout.done();
        }
    }

    public void refund() {
        this.isRefunded = true;
    }
}
