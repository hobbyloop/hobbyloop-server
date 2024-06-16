package com.example.ticketservice.coupon.dto;

import com.example.ticketservice.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponCreateRequestDto {
    private Long minimumPurchaseAmount;

    private Long maximumDiscountAmount;

    private int usableScope;

    private int expirationPeriodDays;

    private int discountType;

    private Long discountAmount;

    private Long discountPercentage;

    private String description;

    private Long totalCount;

    private int maxIssuancePerMember;

    public Coupon toEntity() {
        return Coupon.builder()
                .minimumPurchaseAmount(minimumPurchaseAmount)
                .maximumDiscountAmount(maximumDiscountAmount)
                .usableScope(usableScope)
                .expirationPeriodDays(expirationPeriodDays)
                .discountType(discountType)
                .discountAmount(discountAmount)
                .discountPercentage(discountPercentage)
                .description(description)
                .issueCount(0)
                .usedCount(0)
                .totalCount(totalCount)
                .maxIssuancePerMember(maxIssuancePerMember)
                .build();
    }
}
