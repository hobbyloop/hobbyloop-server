package com.example.ticketservice.point.policy;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;

import java.time.LocalDateTime;

public class PurchasePointPolicy implements PlatformPointPolicy {
    private final double EARN_RATE = 0.05;
    private Long earnAmount;
    private final int EXPIRATION_PERIOD_DAYS = 365;
    private final String DESCRIPTION = "구매 시 기본 5% 적립";

    @Override
    public void calculate(Long totalAmount) {
        earnAmount = (long) (totalAmount * EARN_RATE);
    }

    @Override
    public PointHistory earnOrUse(Point point) {
        point.earn(earnAmount);

        return PointHistory.builder()
                .memberId(point.getMemberId())
                .type(PointTypeEnum.EARN.getValue())
                .amount(earnAmount)
                .balance(point.getBalance())
                .expirationDateTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD_DAYS))
                .description(DESCRIPTION)
                .isProcessedByBatch(false)
                .build();
    }
}
