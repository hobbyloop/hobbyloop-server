package com.example.ticketservice.coupon.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.service.CouponService;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/{centerId}")
    public ResponseEntity<BaseResponseDto<List<CouponResponseDto>>> getCenterCouponList(
            @PathVariable(value = "centerId") Long centerId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(couponService.getCenterCouponListForMember(memberId, centerId)));
    }

    @GetMapping("/members")
    public ResponseEntity<BaseResponseDto<List<MemberCouponResponseDto>>> getAvailableMemberCouponList(
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(couponService.getAvailableMemberCoupons(memberId)));
    }

    @PostMapping("/issue/{couponId}")
    public ResponseEntity<BaseResponseDto<Long>> issueSingleCoupon(
            @PathVariable(value = "couponId") Long couponId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(couponService.issueSingleCoupon(memberId, couponId)));
    }


}
