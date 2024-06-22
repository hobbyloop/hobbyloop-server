package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.ticketservice.ticket.entity.QTicket.ticket;

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
                    .orderBy(ticket.calculatedPrice.asc())
                    .limit(1)
                    .fetchOne());
    }

    @Override
    public List<Ticket> getTicketListByCategoryAroundMe(int category, int sortId, int refundable, double score) {
        return queryFactory
                .selectFrom(ticket)
                .where(ticket.category.eq(category)
                        .and(ticket.score.goe(score))
                        .and(isRefundableTicket(refundable)))
                .orderBy(createOrderSpecifier(sortId))
                .fetch();
    }

    @Override
    public List<Ticket> getTicketListByCategory(int category, int sortId, int refundable, double score, int pageNo, List<String> locations) {
        return queryFactory
                .selectFrom(ticket)
                .where(ticket.category.eq(category)
                        .and(ticket.score.goe(score))
                        .and(isRefundableTicket(refundable))
                        .and(isIncludedLocation(locations)))
                .limit(20)
                .offset(pageNo * 20L)
                .orderBy(createOrderSpecifier(sortId))
                .fetch();
    }

    @Override
    public PurchaseHistoryInOneWeekResponseDto getTicketHighestIssueCount(long centerId) {
        return queryFactory
                .select(Projections.constructor(PurchaseHistoryInOneWeekResponseDto.class,
                                ticket.id,
                                ticket.name,
                                ticket.category,
                                ticket.calculatedPrice
                        )
                )
                .from(ticket)
                .where(ticket.centerId.eq(centerId))
                .orderBy(ticket.issueCount.desc())
                .limit(1)
                .fetchOne();
    }

    private BooleanExpression isRefundableTicket(int refundable) {
        return refundable == 1 ? ticket.isRefundable.eq(true) : null;
    }

    private BooleanExpression ltTicketId(long ticketId) {
        return ticketId != -1 ? ticket.id.lt(ticketId) : null;
    }

    private BooleanExpression isIncludedLocation(List<String> locations) {
        if (locations == null || locations.isEmpty()) {
            return null;
        } else {
            BooleanExpression predicate = null;
            for (String location : locations) {
                if (predicate == null) {
                    predicate = ticket.address.contains(location);
                } else {
                    predicate = predicate.or(ticket.address.contains(location));
                }
            }
            return predicate;
        }
    }

    private OrderSpecifier[] createOrderSpecifier(int sortId) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (sortId == 0) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, ticket.score));
        } else if (sortId == 1) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, ticket.createdAt));
        } else if (sortId == 2) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, ticket.issueCount));
        } else {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, ticket.reviewCount));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
