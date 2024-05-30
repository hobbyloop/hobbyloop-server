package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.UserTicket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.ticketservice.ticket.entity.QTicket.ticket;
import static com.example.ticketservice.ticket.entity.QUserTicket.userTicket;

@RequiredArgsConstructor
public class UserTicketRepositoryImpl implements UserTicketRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserTicket> findAvailableUserTicketList(long memberId) {
        LocalDate now = LocalDate.now();

        return queryFactory
                .selectFrom(userTicket)
                .join(userTicket.ticket, ticket).fetchJoin()
                .where(
                        userTicket.isApprove.isTrue(),
                        userTicket.startDate.loe(now),
                        userTicket.endDate.goe(now),
                        userTicket.remainingCount.gt(0),
                        userTicket.memberId.eq(memberId)
                )
                .fetch();
    }
}
