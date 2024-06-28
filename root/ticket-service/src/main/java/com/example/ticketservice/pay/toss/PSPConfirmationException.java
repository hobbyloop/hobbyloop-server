package com.example.ticketservice.pay.toss;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
}
