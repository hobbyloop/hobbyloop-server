package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckoutResponseDto {
    @Schema(description = "체크아웃 아이디", example = "1")
    private Long checkoutId;

    @Schema(description = "생성된 결제 아이디", example = "1")
    private Long paymentId;

    @Schema(description = "멱등성 키")
    private String idempotencyKey;

    private int type;

    private int method;

    @Schema(description = "최종 결제 금액", example = "95000")
    private Long finalAmount;

    public static CheckoutResponseDto of(Checkout checkout, Payment payment) {
        return CheckoutResponseDto.builder()
                .checkoutId(checkout.getId())
                .paymentId(payment.getId())
                .idempotencyKey(checkout.getIdempotencyKey())
                .type(checkout.getType())
                .method(checkout.getMethod())
                .finalAmount(checkout.getFinalAmount())
                .build();
    }
}
