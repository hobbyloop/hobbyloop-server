package com.example.ticketservice.coupon.repository.projection;

import com.example.ticketservice.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponProjection {
    private Coupon coupon;
    private boolean isAlreadyIssued;

    public CouponProjection(Coupon coupon, Long issuedCount) {
        this.coupon = coupon;
        this.isAlreadyIssued = issuedCount >= coupon.getMaxIssuancePerMember();
    }
}
