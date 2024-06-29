package com.example.ticketservice.pay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentConfirmResponseDto {
    private String status;
    private String errorCode;
    private String errorMessage;
}
