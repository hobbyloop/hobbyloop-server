package com.example.ticketservice.coupon.dto;

import com.example.ticketservice.coupon.entity.MemberCoupon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponResponseDto {
    @Schema(description = "발급된 쿠폰 아이디", example = "1")
    private Long memberCouponId;

    @Schema(description = "최소 구매 금액", example = "75000")
    private Long minimumPurchaseAmount;

    @Schema(description = "최대 할인 가능 금액", example = "25000")
    private Long maximumDiscountAmount;

    @Schema(description = "사용 가능 범위(1: GENERAL, 2: SPECIFIC_COMPANY, 3: SPECIFIC_CENTER)", example = "1")
    private int usableScope;

    @Schema(description = "만료일자")
    private LocalDateTime expirationDateTime;

    @Schema(description = "할인 유형(1: 원, 2: 퍼센트)", example = "1")
    private int discountType;

    @Schema(description = "할인금액(discountType이 1일 때만 필수, 2면 Null)", example = "20000")
    private Long discountAmount;

    @Schema(description = "할인율(discountType이 2일 때만 필수, 1이면 Null)", example = "15")
    private Long discountPercentage;

    @Schema(description = "설명", example = "첫구매 할인 쿠폰")
    private String description;

    public static MemberCouponResponseDto from(MemberCoupon memberCoupon) {
        return MemberCouponResponseDto.builder()
                .memberCouponId(memberCoupon.getId())
                .minimumPurchaseAmount(memberCoupon.getCoupon().getMinimumPurchaseAmount())
                .maximumDiscountAmount(memberCoupon.getCoupon().getMaximumDiscountAmount())
                .usableScope(memberCoupon.getCoupon().getUsableScope())
                .expirationDateTime(memberCoupon.getExpirationDateTime())
                .discountType(memberCoupon.getCoupon().getDiscountType())
                .discountAmount(memberCoupon.getCoupon().getDiscountAmount())
                .discountPercentage(memberCoupon.getCoupon().getDiscountPercentage())
                .description(memberCoupon.getCoupon().getDescription())
                .build();
    }
}
