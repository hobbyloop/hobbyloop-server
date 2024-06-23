package com.example.ticketservice.coupon.entity;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.coupon.entity.vo.CenterInfo;
import com.example.ticketservice.coupon.entity.vo.CompanyInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Coupon extends TimeStamped {
    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long minimumPurchaseAmount;

    private Long maximumDiscountAmount;

    // TODO: 사용가능 범위 정책에 따라 companyId, centerId 등 필드 추가되거나 설계 바뀔 수 있음
    private int usableScope;

    private Long companyId;

    private Long centerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "excluded_companies", joinColumns = @JoinColumn(name = "coupon_id"))
    private List<CompanyInfo> excludedCompanies = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "excluded_centers", joinColumns = @JoinColumn(name = "coupon_id"))
    private List<CenterInfo> excludedCenters = new ArrayList<>();

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime; // 상시일 경우 9999-12-31

    private int expirationPeriodDays;

    private int discountType;

    private Long discountAmount;

    private Long discountPercentage;

    private String description;

    private int issueCount;

    private int usedCount;

    private int maxIssuancePerMember;

    private Long totalCount;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (discountAmount != null && discountPercentage != null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }

        if (isAmountDiscount() && discountAmount == null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }

        if (isPercentageDiscount() && discountPercentage == null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }

        if (isSpecificCompanyScope() && companyId == null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }

        if (isSpecificCenterScope() && centerId == null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }
    }

    public void checkForIssue() {
        LocalDateTime now = LocalDateTime.now();

        // 기한 지남
        if (startDateTime.isAfter(now) || endDateTime.isBefore(now)) {
            throw new ApiException(ExceptionEnum.COUPON_OUT_OF_DATE_EXCEPTION);
        }

        // 더이상 발급할 수 없음
        if (issueCount >= totalCount) {
            throw new ApiException(ExceptionEnum.COUPON_SOLD_OUT_EXCEPTION);
        }
    }

    public void issue() {
        issueCount++;
    }

    public boolean isGeneralScope() {
        return usableScope == CouponUsableScope.GENERAL.getValue();
    }

    public boolean isSpecificCompanyScope() {
        return usableScope == CouponUsableScope.SPECIFIC_COMPANY.getValue();
    }

    public boolean isSpecificCenterScope() {
        return usableScope == CouponUsableScope.SPECIFIC_CENTER.getValue();
    }

    public boolean isAmountDiscount() {
        return discountType == CouponDiscountTypeEnum.AMOUNT.getValue();
    }

    public boolean isPercentageDiscount() {
        return discountType == CouponDiscountTypeEnum.PERCENTAGE.getValue();
    }
}
