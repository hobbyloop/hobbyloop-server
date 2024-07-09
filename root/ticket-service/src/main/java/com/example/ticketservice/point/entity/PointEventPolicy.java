package com.example.ticketservice.point.entity;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
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
public class PointEventPolicy extends TimeStamped implements PlatformPointPolicy {
    @Id
    @Column(name = "point_event_policy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int pointExpirationPeriodDays;

    private String description;

    private Integer additionalPointPercentage;

    private Long additionalPointAmount;

    @Transient
    private Long earnAmount = 0L;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (additionalPointPercentage != null && additionalPointAmount != null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        } else if (additionalPointPercentage == null && additionalPointAmount == null) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }
    }

    @Override
    public void calculate(Long totalAmount) {
        if (additionalPointPercentage != null) {
            earnAmount = totalAmount * (additionalPointPercentage / 100);
        } else {
            earnAmount = additionalPointAmount;
        }
    }

    @Override
    public Pair<Point, PointHistory> earn(Points points) {
        points.earn(earnAmount);

        // TODO: usableScope은 어떻게 하지?
        Point point = Point.builder()
                .memberId(points.getMemberId())
                .points(points)
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
                .description(description)
                .build();

        return Pair.of(point, pointHistory);
    }
}
