package com.example.ticketservice.pay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentConfirmRequestDto {
    @Schema(description = "결제 아이디", example = "1")
    private Long paymentId;

    @Schema(description = "PSP가 반환하는 결제 식별 고유 키")
    private String paymentKey;

    @Schema(description = "멱등성 키")
    private String idempotencyKey;

    @Schema(description = "결제 금액", example = "95000")
    private Long amount;

    @Schema(description = "결제사", example = "toss")
    private int psp;
}
