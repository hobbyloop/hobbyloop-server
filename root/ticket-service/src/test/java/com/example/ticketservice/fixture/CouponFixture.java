package com.example.ticketservice.fixture;

import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.entity.CouponDiscountTypeEnum;
import com.example.ticketservice.coupon.entity.CouponUsableScope;

import java.time.LocalDateTime;
import java.util.List;

public class CouponFixture {

    public static CouponCreateRequestDto generalPercentageDiscountCouponCreateRequest() {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                null,
                null,
                List.of(),
                List.of(),
                30,
                CouponDiscountTypeEnum.PERCENTAGE.getValue(),
                null,
                10L,
                "[하비루프] 첫 구매 할인 쿠폰",
                9999L,
                1,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.of(9999, 12, 31, 23, 59, 59)
        );
    }

    public static CouponCreateRequestDto generalPercentageDiscountCouponExcludingCompaniesCreateRequest(List<Long> excludedCompanies) {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                null,
                null,
                excludedCompanies,
                List.of(),
                30,
                CouponDiscountTypeEnum.PERCENTAGE.getValue(),
                null,
                10L,
                "[하비루프] 첫 구매 할인 쿠폰",
                9999L,
                1,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.of(9999, 12, 31, 23, 59, 59)
        );
    }

    public static CouponCreateRequestDto generalAmountDiscountCouponCreateRequest() {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                null,
                null,
                List.of(),
                List.of(),
                30,
                CouponDiscountTypeEnum.AMOUNT.getValue(),
                25000L,
                null,
                "[하비루프] 첫 구매 할인 쿠폰",
                9999L,
                1,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.of(9999, 12, 31, 23, 59, 59)
        );
    }

    public static CouponCreateRequestDto generalOutOfDateCouponCreateRequest() {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                null,
                null,
                List.of(),
                List.of(),
                30,
                CouponDiscountTypeEnum.AMOUNT.getValue(),
                25000L,
                null,
                "[하비루프] 첫 구매 할인 쿠폰",
                9999L,
                1,
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now().minusDays(1)
        );
    }

    public static CouponCreateRequestDto companyPercentageDiscountCouponCreateRequest(Long companyId) {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.GENERAL.getValue(),
                companyId,
                null,
                List.of(),
                List.of(),
                30,
                CouponDiscountTypeEnum.PERCENTAGE.getValue(),
                null,
                15L,
                "[냥냥] 15% 할인 쿠폰",
                9999L,
                1,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.of(9999, 12, 31, 23, 59, 59)
        );
    }

    public static CouponCreateRequestDto centerPercentageDiscountCouponCreateRequest(Long centerId) {
        return new CouponCreateRequestDto(
                70000L,
                25000L,
                CouponUsableScope.SPECIFIC_CENTER.getValue(),
                null,
                centerId,
                List.of(),
                List.of(),
                30,
                CouponDiscountTypeEnum.PERCENTAGE.getValue(),
                null,
                10L,
                "[필라피티 스튜디오 동탄점] 10% 할인 쿠폰",
                9999L,
                1,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.of(9999, 12, 31, 23, 59, 59)
        );
    }
}
