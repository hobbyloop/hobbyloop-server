package com.example.ticketservice.point.policy;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;

import java.time.LocalDateTime;

public class SignupPointPolicy implements PlatformPointPolicy {
    private final Long EARN_AMOUNT = 3000L;
    private final int EXPIRATION_PERIOD_DAYS = 365;
    private final String DESCRIPTION = "회원가입 포인트 적립";

    @Override
    public void calculate(Long totalAmount) {

    }

    @Override
    public PointHistory earnOrUse(Point point) {
        point.earn(EARN_AMOUNT);

        return PointHistory.builder()
                .memberId(point.getMemberId())
                .type(PointTypeEnum.EARN.getValue())
                .amount(EARN_AMOUNT)
                .balance(point.getBalance())
                .expirationDateTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD_DAYS))
                .description(DESCRIPTION)
                .build();
    }
}
