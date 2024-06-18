package com.example.ticketservice.coupon.dto;

import com.example.ticketservice.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponCreateRequestDto {
    private Long minimumPurchaseAmount;

    private Long maximumDiscountAmount;

    private int usableScope;

    private Long companyId;

    private Long centerId;

    private int expirationPeriodDays;

    private int discountType;

    private Long discountAmount;

    private Long discountPercentage;

    private String description;

    private Long totalCount;

    private int maxIssuancePerMember;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    public Coupon toEntity() {
        return Coupon.builder()
                .minimumPurchaseAmount(minimumPurchaseAmount)
                .maximumDiscountAmount(maximumDiscountAmount)
                .usableScope(usableScope)
                .companyId(companyId)
                .centerId(centerId)
                .expirationPeriodDays(expirationPeriodDays)
                .discountType(discountType)
                .discountAmount(discountAmount)
                .discountPercentage(discountPercentage)
                .description(description)
                .issueCount(0)
                .usedCount(0)
                .totalCount(totalCount)
                .maxIssuancePerMember(maxIssuancePerMember)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
}
