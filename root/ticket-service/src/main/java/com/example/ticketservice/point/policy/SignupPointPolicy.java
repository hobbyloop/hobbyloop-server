package com.example.ticketservice.point.policy;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.Points;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public class SignupPointPolicy implements PlatformPointPolicy {
    private final Long EARN_AMOUNT = 3000L;
    private final int EXPIRATION_PERIOD_DAYS = 365;
    private final String DESCRIPTION = "회원가입 포인트 적립";

    @Override
    public void calculate(Long totalAmount) {

    }

    @Override
    public Pair<Point, PointHistory> earn(Points points) {
        points.earn(EARN_AMOUNT);

        LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(EXPIRATION_PERIOD_DAYS);

        Point point = Point.builder()
                .memberId(points.getMemberId())
                .points(points)
                .amount(EARN_AMOUNT)
                .usableScope(PointUsableScopeEnum.GENERAL.getValue())
                .isExpiringSoon(false)
                .expirationDateTime(expirationDateTime)
                .build();

        PointHistory pointHistory = PointHistory.builder()
                .memberId(points.getMemberId())
                .type(PointTypeEnum.EARN.getValue())
                .amount(EARN_AMOUNT)
                .balance(points.getBalance())
                .description(DESCRIPTION)
                .build();

        return Pair.of(point, pointHistory);
    }
}
