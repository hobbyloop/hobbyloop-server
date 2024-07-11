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

    private boolean isReconciled;

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

    // 이거 조건 이렇게 하면 안 됨... 전액환불 + 포인트 + 쿠폰일 때는 이래도 되는데.. 아 조건 복잡해지겠네
    // 부분환불일 때는 포인트만 환급. 쿠폰은 소멸.
    // 전액환불일 때는 포인트 환급, 쿠폰도 반환
    public void complete() {
        // 부분환불
        if (isPartialRefund()) {
            if (this.payment.getCheckout().getPointDiscountAmount() > 0L) {
                if (this.isPointUpdated) {
                    this.isRefundDone = true;
                }
            } else {
                this.isRefundDone = true;
            }
        }
        // 전액환불
        else {
            if (this.payment.getCheckout().getPointDiscountAmount() > 0L
                && this.payment.getCheckout().getCouponDiscountAmount() > 0L) {
                if (this.isCouponUpdated && this.isPointUpdated) {
                    this.isRefundDone = true;
                }
            } else if (this.payment.getCheckout().getPointDiscountAmount() > 0L) {
                if (this.isPointUpdated) {
                    this.isRefundDone = true;
                }
            } else if (this.payment.getCheckout().getCouponDiscountAmount() > 0L) {
                if (this.isCouponUpdated) {
                    this.isRefundDone = true;
                }
            } else {
                this.isRefundDone = true;
            }
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

    public boolean isPartialRefund() {
        return this.payment.getAmount() > this.amount;
    }

    public void reconcile() {
        this.isReconciled = true;
    }
}
