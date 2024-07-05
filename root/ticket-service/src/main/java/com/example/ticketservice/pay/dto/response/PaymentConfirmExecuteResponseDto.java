package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.pay.entity.member.enums.PSPConfirmationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmExecuteResponseDto {
    private String paymentKey;
    private String IdempotencyKey;
    private String type;
    private String method;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;
    private String pspRawData;
    private String orderName;
    private int pspConfirmationStatus;
    private Long totalAmount;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryable;

    public static PaymentConfirmExecuteResponseDto from(TossPaymentConfirmResponseDto responseDto) {
        LocalDateTime canceledAt = null;
        if (responseDto.getCancels() != null && !responseDto.getCancels().isEmpty()) {
            canceledAt = LocalDateTime.parse(responseDto.getCancels().get(0).getCanceledAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        return PaymentConfirmExecuteResponseDto.builder()
                .paymentKey(responseDto.getPaymentKey())
                .IdempotencyKey(responseDto.getOrderId())
                .type(responseDto.getType())
                .method(responseDto.getMethod())
                .approvedAt(LocalDateTime.parse(responseDto.getApprovedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .canceledAt(canceledAt)
                .pspRawData(responseDto.toString())
                .orderName(responseDto.getOrderName())
                .pspConfirmationStatus(PSPConfirmationStatusEnum.get(responseDto.getStatus()).getValue())
                .totalAmount(Long.valueOf(responseDto.getTotalAmount()))
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }
}
