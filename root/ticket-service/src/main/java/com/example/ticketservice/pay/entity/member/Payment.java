package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.toss.PSPConfirmationException;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Checkout checkout;

    private String idempotencyKey;

    private Long centerId; //sellerId

    private Long ticketId;

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
                .idempotencyKey(checkout.getIdempotencyKey())
                .centerId(ticket.getCenterId())
                .ticketId(ticket.getId())
                .amount(checkout.getFinalAmount())
                .status(1)
                .isLedgerUpdated(false)
                .isWalletUpdated(false)
                .failedCount(0)
                .build();
    }

    public void confirm(PaymentConfirmExecuteResponseDto responseDto) {
        this.status = responseDto.getPspConfirmationStatus();
        this.pspRawData = responseDto.getPspRawData();
    }

    public void fail(PSPConfirmationException ex) {
        this.failedCount++;
        this.status = PaymentStatusEnum.FAILURE.getValue();
        this.pspRawData = ex
    }
}
