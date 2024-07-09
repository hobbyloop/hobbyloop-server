package com.example.ticketservice.pay.exception;

import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.toss.TossPaymentException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PSPConfirmationException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryableError;

    public static PSPConfirmationException from(TossPaymentException exception) {
        return PSPConfirmationException.builder()
                .errorCode(exception.name())
                .errorMessage(exception.getDescription())
                .isSuccess(exception.isSuccess())
                .isFailure(exception.isFailure())
                .isUnknown(exception.isUnknown())
                .isRetryableError(exception.isRetryableError())
                .build();
    }

    public boolean isRetryableError() {
        return isRetryableError;
    }

    public PaymentStatusEnum paymentStatus() {
        if (isSuccess) return PaymentStatusEnum.SUCCESS;
        if (isFailure) return PaymentStatusEnum.FAILURE;
        if (isUnknown) return PaymentStatusEnum.UNKNOWN;
        throw new IllegalStateException("올바르지 않은 결제 상태");
    }
}
