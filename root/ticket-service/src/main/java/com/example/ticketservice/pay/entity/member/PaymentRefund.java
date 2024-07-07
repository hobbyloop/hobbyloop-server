package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PaymentRefund extends TimeStamped {
    @Id
    @Column(name = "payment_refund_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    private Long amount;

    private String pspPaymentKey;

    private String pspRawData;

    private int status;

    private boolean isRefundDone;

    private LocalDateTime refundedAt;

    private Boolean isPointUpdated;

    private Boolean isCouponUpdated;

    private int failedCount;

    @Builder.Default
    private int threshold = 3;

    public static PaymentRefund of(Payment payment, Long amount) {
        Boolean isPointUpdated = null;
        Boolean isCouponUpdated = null;
        if (payment.getIsPointUpdated() != null)
            isPointUpdated = false;
        if (payment.getIsCouponUpdated() != null)
            isCouponUpdated = false;

        return PaymentRefund.builder()
                .payment(payment)
                .memberId(payment.getMemberId())
                .amount(amount)
                .status(PaymentStatusEnum.NOT_STARTED.getValue())
                .pspPaymentKey(payment.getPspPaymentKey())
                .isPointUpdated(isPointUpdated)
                .isCouponUpdated(isCouponUpdated)
                .failedCount(0)
                .build();
    }

    public void execute() {
        if (this.payment.getStatus() != PaymentStatusEnum.SUCCESS.getValue()) {
            throw new IllegalStateException("환불할 수 있는 결제가 아닙니다.");
        }

        this.status = PaymentStatusEnum.EXECUTING.getValue();
    }

    public void refund(PaymentConfirmExecuteResponseDto responseDto) {
        this.status = PaymentStatusEnum.SUCCESS.getValue();
        this.refundedAt = responseDto.getCanceledAt();
        this.pspRawData = responseDto.getPspRawData();
    }

    public void refund(LocalDateTime canceledAt, String pspRawData) {
        this.status = PaymentStatusEnum.SUCCESS.getValue();
        this.refundedAt = canceledAt;
        this.pspRawData = pspRawData;
    }

    public void complete() {
        if (isCouponUpdated && isPointUpdated) {
            this.isRefundDone = true;
        }
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

    public void markPointUpdated() {
        this.isPointUpdated = true;
    }

    public void markCouponUpdated() {
        this.isCouponUpdated = true;
    }
}
