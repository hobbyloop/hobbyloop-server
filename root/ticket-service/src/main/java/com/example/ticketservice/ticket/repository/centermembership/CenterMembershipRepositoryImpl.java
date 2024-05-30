package com.example.ticketservice.ticket.repository.centermembership;

import com.example.ticketservice.ticket.entity.CenterMembership;
import com.example.ticketservice.ticket.entity.CenterMembershipStatusEnum;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.ticketservice.ticket.entity.QCenterMembership.centerMembership;

@RequiredArgsConstructor
public class CenterMembershipRepositoryImpl implements CenterMembershipRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CenterMembership> getCenterMembershipListByCenterId(long centerId, int pageNo, int sortId) {
        if (sortId == 2) {
            return queryFactory
                    .selectFrom(centerMembership)
                    .where(centerMembership.centerId.eq(centerId).and(centerMembership.status.eq(CenterMembershipStatusEnum.EXPIRING_SOON.getStatusType())))
                    .limit(20)
                    .offset(pageNo * 20)
                    .orderBy(centerMembership.updatedAt.desc())
                    .fetch();
        }

        return queryFactory
                .selectFrom(centerMembership)
                .where(centerMembership.centerId.eq(centerId))
                .limit(20)
                .offset(pageNo * 20)
                .orderBy(createOrderSpecifier(sortId))
                .fetch();
    }

    private OrderSpecifier[] createOrderSpecifier(int sortId) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (sortId == 0) { // 이름순
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, centerMembership.memberName));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
