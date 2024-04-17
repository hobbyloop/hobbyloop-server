package com.example.ticketservice.pay.repository.purchasehistory;

import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.example.ticketservice.entity.QTicket.ticket;
import static com.example.ticketservice.pay.entity.QPurchaseHistory.purchaseHistory;

@RequiredArgsConstructor
public class PurchaseHistoryRepositoryImpl implements PurchaseHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PurchaseHistoryInOneWeekResponseDto getHotTicketIdInOneWeek(long centerId) {
        NumberPath<Long> aliasCount = Expressions.numberPath(Long.class, "count");

        return queryFactory
                    .select(Projections.constructor(PurchaseHistoryInOneWeekResponseDto.class,
                            ticket.id,
                            ticket.name,
                            ticket.category,
                            ticket.calculatedPrice,
                            ticket.id.count().as(aliasCount)))
                    .from(purchaseHistory)
                    .join(purchaseHistory.ticket, ticket)
                    .where(purchaseHistory.ticket.centerId.eq(centerId)
                            .and(inOneWeek()))
                    .groupBy(ticket.id, ticket.name, ticket.category, ticket.calculatedPrice)
                    .orderBy(aliasCount.desc())
                    .limit(1)
                    .fetchOne();
    }

    private BooleanExpression inOneWeek() {
        return purchaseHistory.date.goe(LocalDateTime.now().minusDays(7));
    }
}
