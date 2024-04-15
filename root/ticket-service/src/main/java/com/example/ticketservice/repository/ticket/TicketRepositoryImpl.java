package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.Ticket;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.ticketservice.entity.QTicket.ticket;

@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> getTicketList(long centerId, long ticketId) {
        return queryFactory
                .selectFrom(ticket)
                .where(ticket.centerId.eq(centerId).and(ltTicketId(ticketId)))
                .limit(20)
                .orderBy(ticket.id.desc())
                .fetch();
    }

    @Override
    public Optional<Ticket> getMinimumPriceTicket(long centerId) {
        return Optional.ofNullable(
                queryFactory
                    .selectFrom(ticket)
                    .where(ticket.centerId.eq(centerId))
                    .orderBy(ticket.calculatedPrice.desc())
                    .limit(1)
                    .fetchOne());
    }

    private BooleanExpression ltTicketId(long ticketId) {
        return ticketId != -1 ? ticket.id.lt(ticketId) : null;
    }
}
