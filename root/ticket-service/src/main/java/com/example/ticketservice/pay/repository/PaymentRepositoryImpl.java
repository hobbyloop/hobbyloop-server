package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.ticketservice.pay.entity.member.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Payment> findPendingPayments(LocalDateTime updatedAt) {
        LocalDateTime threeMinutesAgo = updatedAt.minusMinutes(3);

        return queryFactory
                .selectFrom(payment)
                .where(
                        payment.status.eq(PaymentStatusEnum.UNKNOWN.getValue())
                                .or(payment.status.eq(PaymentStatusEnum.EXECUTING.getValue())
                                        .and(payment.updatedAt.loe(threeMinutesAgo))
                                )
                )
                .where(payment.failedCount.lt(payment.threshold))
                .limit(10)
                .fetch();
    }
}
