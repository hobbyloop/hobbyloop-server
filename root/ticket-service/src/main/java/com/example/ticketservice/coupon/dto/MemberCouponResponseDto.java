package com.example.ticketservice.coupon.dto;

import com.example.ticketservice.coupon.entity.MemberCoupon;
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
    private Long memberCouponId;

    private Long minimumPurchaseAmount;

    private Long maximumDiscountAmount;

    private int usableScope;

    private LocalDateTime expirationDateTime;

    private int discountType;

    private Long discountAmount;

    private Long discountPercentage;

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
