package com.example.ticketservice.coupon.service;

import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;

import java.util.List;

public interface CouponService {
    Long createCoupon(CouponCreateRequestDto request);

    Long getCountOfAvailableMemberCoupons(Long memberId);

    List<MemberCouponResponseDto> getAvailableMemberCoupons(Long memberId);

    List<CouponResponseDto> getCenterCouponListForMember(Long memberId, Long centerId);

    Long issueSingleCoupon(Long memberId, Long couponId);

    void issueAllCoupons(Long memberId, List<Long> couponIds);
}
