package com.example.ticketservice.coupon.dto;

import com.example.ticketservice.coupon.entity.Coupon;
import com.example.ticketservice.coupon.entity.vo.CenterInfo;
import com.example.ticketservice.coupon.entity.vo.CompanyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CouponCreateRequestDto {
    @Schema(description = "최소 구매 금액", example = "75000")
    private Long minimumPurchaseAmount;

    @Schema(description = "최대 할인 가능 금액", example = "20000")
    private Long maximumDiscountAmount;

    @Schema(description = "사용 가능 범위(1: 전체, 2: 특정 업체, 3: 특정 시설)", example = "1")
    private int usableScope;

    @Schema(description = "업체 아이디(usableScope이 2일 때만 필수. 아니면 Null)", example = "1")
    private Long companyId;

    @Schema(description = "시설 아이디(usableScope이 3일 때만 필수, 아니면 Null)", example = "1")
    private Long centerId;

    @Schema(description = "사용 가능 범위에서 제외할 업체 아이디 목록")
    private List<Long> excludedCompanyIds;

    @Schema(description = "사용 가능 범위에서 제외할 시설 아이디 목록")
    private List<Long> excludedCenterIds;

    @Schema(description = "쿠폰 사용 가능 일(쿠폰발급일자 + expirationPeriodDays = expirationDateTime)", example = "30")
    private int expirationPeriodDays;

    @Schema(description = "할인 유형(1: 원, 2: 퍼센트)", example = "1")
    private int discountType;

    @Schema(description = "할인 금액(discountType이 1일 때만 필수. 아니면 Null)", example = "20000")
    private Long discountAmount;

    @Schema(description = "할인율(discountType이 2일 때만 필수. 아니면 Null)", example = "10")
    private Long discountPercentage;

    @Schema(description = "쿠폰 설명", example = "첫 구매 할인 쿠폰")
    private String description;

    @Schema(description = "총 개수, 이 이상 발급할 수 없음", example = "1000")
    private Long totalCount;

    @Schema(description = "한 명당 최대 발급 가능 횟수", example = "1")
    private int maxIssuancePerMember;

    @Schema(description = "쿠폰 유효기간 - 시작일(해당 기한 내에만 회원이 쿠폰을 발급받을 수 있음을 나타냄)")
    private LocalDateTime startDateTime;

    @Schema(description = "쿠폰 유효기간 - 종료일")
    private LocalDateTime endDateTime;

    public Coupon toEntity(List<CompanyInfo> excludedCompanies, List<CenterInfo> excludedCenters) {
        return Coupon.builder()
                .minimumPurchaseAmount(minimumPurchaseAmount)
                .maximumDiscountAmount(maximumDiscountAmount)
                .usableScope(usableScope)
                .companyId(companyId)
                .centerId(centerId)
                .excludedCompanies(excludedCompanies)
                .excludedCenters(excludedCenters)
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
