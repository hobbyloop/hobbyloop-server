package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.exception.PaymentAlreadyProcessedException;
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

    private Long centerId; //sellerId

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    private Long looppassId;

    private Long amount;

    private int psp; // toss, ... // TODO: 이거 언제 업뎃함?

    private String pspRawData;

    private int status; // not_started, executing, success

    private boolean isLedgerUpdated;

    private boolean isWalletUpdated;

    private int failedCount;

    @Builder.Default
    private int threshold = 3;

    public static Payment checkout(Checkout checkout, Ticket ticket) {
        return Payment.builder()
                .checkout(checkout)
                .memberId(checkout.getMemberId())
                .idempotencyKey(checkout.getIdempotencyKey())
                .centerId(ticket.getCenterId())
                .ticket(ticket)
                .amount(checkout.getFinalAmount())
                .status(1)
                .isLedgerUpdated(false)
                .isWalletUpdated(false)
                .failedCount(0)
                .build();
    }

    public void confirm(PaymentConfirmExecuteResponseDto responseDto) {
        this.status = PaymentStatusEnum.SUCCESS.getValue();
        this.pspRawData = responseDto.getPspRawData();
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

    public void execute() {
        if (this.status == PaymentStatusEnum.SUCCESS.getValue()) {
            throw new PaymentAlreadyProcessedException(PaymentStatusEnum.SUCCESS, "이미 성공한 결제입니다.");
        } else if (this.status == PaymentStatusEnum.FAILURE.getValue()) {
            throw new PaymentAlreadyProcessedException(PaymentStatusEnum.FAILURE, "이미 실패한 결제입니다.");
        }

        this.status = PaymentStatusEnum.EXECUTING.getValue();
    }
}
