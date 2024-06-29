package com.example.ticketservice.pay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckoutRequestDto {
    @Schema(description = "생성된 체크아웃 아이디(이 아이디로 체크아웃 요청)", example = "1")
    private Long checkoutId;

    @Schema(description = "중복 요청 방지를 위한 멱등성 키, 3초 이내의 동일한 요청은 이미 생성된 체크아웃 데이터 활용", example = "randomString")
    private String idempotencyKey;

    @Schema(description = "결제 유형, 아직 안 정해짐.. 무조건 일반결제로 함(추후 루프패스 구현되면 추가될 수도 있음)")
    private int type;

    @Schema(description = "결제 방식. 아직 안 정해짐.. 무조건 토스 간편결제로 함")
    private int method;

    @Schema(description = "사용할 쿠폰 아이디", example = "1")
    private Long memberCouponId;

    @Schema(description = "사용할 포인트", example = "2000")
    private Long points;

    @Schema(description = "총 할인된 금액", example = "3500")
    private Long totalDiscountAmount;

    @Schema(description = "최종 결제 금액", example = "95000")
    private Long finalAmount;

    @Schema(description = "쿠폰 할인 금액", example = "1000")
    private Long couponDiscountAmount;
}
