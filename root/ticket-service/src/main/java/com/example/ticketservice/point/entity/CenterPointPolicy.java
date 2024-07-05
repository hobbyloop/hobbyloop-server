package com.example.ticketservice.point.entity;

import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.point.policy.PlatformPointPolicy;
import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterPointPolicy extends TimeStamped implements PlatformPointPolicy {
    @Id
    @Column(name = "center_point_policy")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long centerId;

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
    public Pair<Point, PointHistory> earn(Points points) {
        points.earn(earnAmount);

        Point point = Point.builder()
                .memberId(points.getMemberId())
                .points(points)
                .centerId(points.getCenterId())
                .usableScope(points.getUsableScope())
                .amount(earnAmount)
                .expirationDateTime(LocalDateTime.now().plusDays(pointExpirationPeriodDays))
                .isExpiringSoon(false)
                .build();

        PointHistory pointHistory = PointHistory.builder()
                .memberId(points.getMemberId())
                .type(PointTypeEnum.EARN.getValue())
                .amount(earnAmount)
                .balance(points.getBalance())
                .description("시설 추가 적립") // TODO: 문구 고쳐야함(centerName, ticketName 가져와서)
                .build();

        return Pair.of(point, pointHistory);
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
