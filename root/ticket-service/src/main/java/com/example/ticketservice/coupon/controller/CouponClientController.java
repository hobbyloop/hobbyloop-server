package com.example.ticketservice.coupon.controller;

import com.example.ticketservice.coupon.service.CouponService;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons/client")
public class CouponClientController {
    private final CouponService couponService;

    @GetMapping("/my/count/{memberId}")
    public ResponseEntity<BaseResponseDto<Long>> getCountOfAvailableMemberCoupons(
            @PathVariable(value = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(couponService.getCountOfAvailableMemberCoupons(memberId)));
    }
}
