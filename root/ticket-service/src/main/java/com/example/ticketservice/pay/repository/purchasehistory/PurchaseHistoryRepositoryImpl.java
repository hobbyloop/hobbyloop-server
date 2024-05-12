package com.example.ticketservice.pay.repository.purchasehistory;

import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.ticketservice.entity.QTicket.ticket;
import static com.example.ticketservice.pay.entity.QPurchaseHistory.purchaseHistory;

@RequiredArgsConstructor
public class PurchaseHistoryRepositoryImpl implements PurchaseHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PurchaseHistoryInOneWeekResponseDto> getHotTicketIdInOneWeek(long centerId) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(PurchaseHistoryInOneWeekResponseDto.class,
                                        ticket.id,
                                        ticket.name,
                                        ticket.category,
                                        ticket.calculatedPrice
                                )
                        )
                        .from(purchaseHistory)
                        .join(purchaseHistory.ticket, ticket)
                        .where(purchaseHistory.ticket.centerId.eq(centerId)
                                .and(inOneWeek()))
                        .groupBy(purchaseHistory.ticket.id)
                        .orderBy(purchaseHistory.ticket.id.count().desc())
                        .limit(1)
                        .fetchOne()
        );
    }

    private BooleanExpression inOneWeek() {
        return purchaseHistory.date.goe(LocalDateTime.now().minusDays(7));
    }
}
