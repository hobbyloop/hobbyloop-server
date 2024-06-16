package com.example.ticketservice.coupon.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.service.CouponService;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/coupons")
public class AdminCouponController {
    private final CouponService couponService;

    // TODO: 쿠폰 등록 주체를 명확하게 구분해야 하는데...
    // 플랫폼 쪽 관리자? 업체 관리자? 어쩌구... 어떡하지. 그걸 어케 알지.
    @PostMapping
    public ResponseEntity<BaseResponseDto<Long>> createCoupon(
            @RequestBody CouponCreateRequestDto requestDto,
            HttpServletRequest request
            ) {
        Long id = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(couponService.createCoupon(requestDto)));
    }
}
