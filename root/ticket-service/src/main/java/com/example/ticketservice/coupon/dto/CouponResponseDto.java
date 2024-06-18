package com.example.ticketservice.coupon.dto;

import com.example.ticketservice.coupon.repository.projection.CouponProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponseDto {
    private Long couponId;

    private boolean alreadyIssued;

    private Long minimumPurchaseAmount;

    private Long maximumDiscountAmount;

    private int usableScope;

    private int expirationPeriodDays;

    private int discountType;

    private Long discountAmount;

    private Long discountPercentage;

    private String description;

    public static CouponResponseDto from(CouponProjection couponProjection) {
        return CouponResponseDto.builder()
                .couponId(couponProjection.getCoupon().getId())
                .alreadyIssued(couponProjection.isAlreadyIssued())
                .minimumPurchaseAmount(couponProjection.getCoupon().getMinimumPurchaseAmount())
                .maximumDiscountAmount(couponProjection.getCoupon().getMaximumDiscountAmount())
                .usableScope(couponProjection.getCoupon().getUsableScope())
                .expirationPeriodDays(couponProjection.getCoupon().getExpirationPeriodDays())
                .discountType(couponProjection.getCoupon().getDiscountType())
                .discountAmount(couponProjection.getCoupon().getDiscountAmount())
                .discountPercentage(couponProjection.getCoupon().getDiscountPercentage())
                .description(couponProjection.getCoupon().getDescription())
                .build();
    }
}
