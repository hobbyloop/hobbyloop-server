package com.example.ticketservice.fixture;

import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.entity.CouponDiscountTypeEnum;
import com.example.ticketservice.coupon.entity.CouponUsableScope;

public class CouponFixture {

    public static CouponCreateRequestDto generalPercentageDiscountCouponCreateRequest() {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                30,
                CouponDiscountTypeEnum.PERCENTAGE.getValue(),
                null,
                10L,
                "[하비루프] 첫 구매 할인 쿠폰",
                9999L,
                1
        );
    }

    public static CouponCreateRequestDto generalAmountDiscountCouponCreateRequest() {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                30,
                CouponDiscountTypeEnum.AMOUNT.getValue(),
                25000L,
                null,
                "[하비루프] 첫 구매 할인 쿠폰",
                9999L,
                1
        );
    }

    public static CouponCreateRequestDto companyPercentageDiscountCouponCreateRequest() {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                30,
                CouponDiscountTypeEnum.PERCENTAGE.getValue(),
                null,
                15L,
                "[냥냥] 15% 할인 쿠폰",
                9999L,
                1
        );
    }
}
