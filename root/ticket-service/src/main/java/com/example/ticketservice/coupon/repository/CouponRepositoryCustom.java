package com.example.ticketservice.coupon.repository;

import com.example.ticketservice.coupon.repository.projection.CouponProjection;

import java.util.List;

public interface CouponRepositoryCustom {
    List<CouponProjection> findCenterCouponList(Long memberId, Long companyId, Long centerId);
}
