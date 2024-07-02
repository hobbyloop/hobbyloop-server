package com.example.ticketservice.pay.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmResponseDto {
    @Schema(description = "결제 승인 상태(SUCCESS, FAILURE, UNKNOWN)", example = "SUCCESS")
    private String status;
    @Schema(description = "에러 코드")
    private String errorCode;
    @Schema(description = "에러 메세지")
    private String errorMessage;
}
