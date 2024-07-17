package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.entity.member.enums.PSPEnum;
import com.example.ticketservice.pay.entity.member.enums.TransactionErrorStatusEnum;
import com.example.ticketservice.pay.entity.member.enums.TransactionErrorTypeEnum;
import com.example.ticketservice.pay.toss.TossTransactionsResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class TransactionError extends TimeStamped {
    @Id
    @Column(name = "transaction_error_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pspTransactionKey;

    private LocalDateTime transactionAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentRefund refund;

    private Long pspAmount;

    private Long recordedAmount;

    private Long diffAmount;

    private String pspStatus;

    private Integer recordedStatus;

    private int psp;

    private int type;

    private int status;

    private String description;

    private LocalDateTime resolvedAt;

    public void resolve() {
        this.status = TransactionErrorStatusEnum.RESOLVED.getValue();
        this.resolvedAt = LocalDateTime.now();
    }

    public static TransactionError mismatchOf(TossTransactionsResponseDto transaction, Payment payment) {
        return TransactionError.builder()
                .pspTransactionKey(transaction.getTransactionKey())
                .psp(PSPEnum.TOSS.getValue())
                .payment(payment)
                .status(TransactionErrorStatusEnum.OPEN.getValue())
                .type(TransactionErrorTypeEnum.AMOUNT_MISMATCH.getValue())
                .pspAmount(Long.valueOf(transaction.getAmount()))
                .recordedAmount(payment.getAmount())
                .diffAmount(transaction.getAmount() - payment.getAmount())
                .description("PG사 금액 불일치")
                .transactionAt(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static TransactionError mismatchOf(TossTransactionsResponseDto transaction, PaymentRefund refund) {
        return TransactionError.builder()
                .pspTransactionKey(transaction.getTransactionKey())
                .psp(PSPEnum.TOSS.getValue())
                .refund(refund)
                .status(TransactionErrorStatusEnum.OPEN.getValue())
                .type(TransactionErrorTypeEnum.AMOUNT_MISMATCH.getValue())
                .pspAmount(Long.valueOf(transaction.getAmount()))
                .recordedAmount(refund.getAmount())
                .diffAmount(transaction.getAmount() - refund.getAmount())
                .description("PG사 금액 불일치")
                .transactionAt(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static TransactionError paymentNotFoundOf(TossTransactionsResponseDto transaction) {
        return TransactionError.builder()
                .pspTransactionKey(transaction.getTransactionKey())
                .psp(PSPEnum.TOSS.getValue())
                .status(TransactionErrorStatusEnum.OPEN.getValue())
                .type(TransactionErrorTypeEnum.PAYMENT_NOT_FOUND.getValue())
                .pspAmount(Long.valueOf(transaction.getAmount()))
                .description("결제 누락")
                .transactionAt(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static TransactionError refundNotFoundOf(TossTransactionsResponseDto transaction) {
        return TransactionError.builder()
                .pspTransactionKey(transaction.getTransactionKey())
                .psp(PSPEnum.TOSS.getValue())
                .status(TransactionErrorStatusEnum.OPEN.getValue())
                .type(TransactionErrorTypeEnum.REFUND_NOT_FOUND.getValue())
                .pspAmount(Long.valueOf(transaction.getAmount()))
                .description("환불 누락")
                .transactionAt(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static TransactionError statusMismatchOf(TossTransactionsResponseDto transaction, Payment payment) {
        return TransactionError.builder()
                .pspTransactionKey(transaction.getTransactionKey())
                .psp(PSPEnum.TOSS.getValue())
                .status(TransactionErrorStatusEnum.OPEN.getValue())
                .type(TransactionErrorTypeEnum.STATUS_MISMATCH.getValue())
                .pspStatus(transaction.getStatus())
                .recordedStatus(payment.getStatus())
                .description("상태 불일치")
                .transactionAt(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
}
