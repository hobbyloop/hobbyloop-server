package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.pay.entity.member.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class PaymentConfirmExecuteResponseDto {
    private String paymentKey;
    private String IdempotencyKey;
    private String type;
    private String method;
    private LocalDateTime approvedAt;
    private String pspRawData;
    private String orderName;
    private int pspConfirmationStatus;
    private Long totalAmount;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryable;

    public static PaymentConfirmExecuteResponseDto from(TossPaymentConfirmResponseDto responseDto) {
        return PaymentConfirmExecuteResponseDto.builder()
                .paymentKey(responseDto.getPaymentKey())
                .IdempotencyKey(responseDto.getOrderId())
                .type(responseDto.getType())
                .method(responseDto.getMethod())
                .approvedAt(LocalDateTime.parse(responseDto.getApprovedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .pspRawData(responseDto.toString())
                .orderName(responseDto.getOrderName())
                .pspConfirmationStatus(responseDto.getStatus())
                .totalAmount(responseDto.getTotalAmount())
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }
}
