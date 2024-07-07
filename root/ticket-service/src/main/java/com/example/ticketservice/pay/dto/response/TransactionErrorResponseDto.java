package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.pay.entity.member.TransactionError;
import com.example.ticketservice.pay.entity.member.enums.PSPEnum;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.entity.member.enums.TransactionErrorStatusEnum;
import com.example.ticketservice.pay.entity.member.enums.TransactionErrorTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionErrorResponseDto {
    private String pspTransactionKey;
    private LocalDateTime transactionAt;
    private Long pspAmount;
    private Long recordedAmount;
    private Long diffAmount;
    private String pspStatus;
    private String recordedStatus;
    private String psp;
    private String type;
    private String status;
    private String description;
    private LocalDateTime createdAt;

    public static TransactionErrorResponseDto from(TransactionError transactionError) {
        return TransactionErrorResponseDto.builder()
                .pspTransactionKey(transactionError.getPspTransactionKey())
                .transactionAt(transactionError.getTransactionAt())
                .pspAmount(transactionError.getPspAmount())
                .recordedAmount(transactionError.getRecordedAmount())
                .diffAmount(transactionError.getDiffAmount())
                .pspStatus(transactionError.getPspStatus())
                .recordedStatus(PaymentStatusEnum.findByValue(transactionError.getRecordedStatus()).name())
                .psp(PSPEnum.findByValue(transactionError.getPsp()).name())
                .type(TransactionErrorTypeEnum.findByValue(transactionError.getType()).name())
                .status(TransactionErrorStatusEnum.findByValue(transactionError.getStatus()).name())
                .description(transactionError.getDescription())
                .createdAt(transactionError.getCreatedAt())
                .build();
    }
}
