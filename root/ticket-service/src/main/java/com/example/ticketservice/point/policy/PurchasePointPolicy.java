package com.example.ticketservice.point.policy;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.Points;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public class PurchasePointPolicy implements PlatformPointPolicy {
    private final double EARN_RATE = 0.015;
    private Long earnAmount;
    private final int EXPIRATION_PERIOD_DAYS = 365;
    private final String DESCRIPTION = "구매 시 기본 1.5% 적립";

    @Override
    public void calculate(Long totalAmount) {
        earnAmount = (long) (totalAmount * EARN_RATE);
    }

    @Override
    public Pair<Point, PointHistory> earn(Points points) {
        points.earn(earnAmount);

        Point point = Point.builder()
                .memberId(points.getMemberId())
                .points(points)
                .usableScope(points.getUsableScope())
                .amount(earnAmount)
                .expirationDateTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD_DAYS))
                .isExpiringSoon(false)
                .build();

        PointHistory pointHistory =  PointHistory.builder()
                .memberId(points.getMemberId())
                .type(PointTypeEnum.EARN.getValue())
                .amount(earnAmount)
                .balance(points.getBalance())
                .description(DESCRIPTION)
                .build();

        return Pair.of(point, pointHistory);
    }
}
