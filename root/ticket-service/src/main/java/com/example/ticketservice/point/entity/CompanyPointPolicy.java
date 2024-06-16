package com.example.ticketservice.point.entity;

import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.point.policy.PlatformPointPolicy;
import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CompanyPointPolicy extends TimeStamped implements PlatformPointPolicy {
    @Id
    @Column(name = "company_point_policy")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private int usableScope;

    private int additionalPointPercentage;

    private int pointExpirationPeriodDays;

    @Transient
    private Long earnAmount;

    @Override
    public void calculate(Long totalAmount) {
        earnAmount = totalAmount * (additionalPointPercentage / 100);
    }

    @Override
    public PointHistory earnOrUse(Point point) {
        point.earn(earnAmount);

        return PointHistory.builder()
                .memberId(point.getMemberId())
                .type(PointTypeEnum.EARN.getValue())
                .amount(earnAmount)
                .balance(point.getBalance())
                .expirationDateTime(LocalDateTime.now().plusDays(pointExpirationPeriodDays))
                .description("업체 추가 적립") // TODO: 문구 고쳐야함
                .isProcessedByBatch(false)
                .build();
    }

    public boolean isGeneralScope() {
        return usableScope == PointUsableScopeEnum.GENERAL.getValue();
    }

    public boolean isSpecificCompanyScope() {
        return usableScope == PointUsableScopeEnum.SPECIFIC_COMPANY.getValue();
    }

    public boolean isSpecificCenterScope() {
        return usableScope == PointUsableScopeEnum.SPECIFIC_CENTER.getValue();
    }
}
