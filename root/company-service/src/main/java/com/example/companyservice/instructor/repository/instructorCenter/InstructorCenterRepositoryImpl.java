package com.example.companyservice.instructor.repository.instructorCenter;

import com.example.companyservice.instructor.entity.InstructorCenter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.companyservice.company.entity.QCenter.center;
import static com.example.companyservice.instructor.entity.QInstructor.instructor;
import static com.example.companyservice.instructor.entity.QInstructorCenter.instructorCenter;

@RequiredArgsConstructor
public class InstructorCenterRepositoryImpl implements InstructorCenterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<InstructorCenter> getInstructorCenterList(long centerId, int sort) {
        return queryFactory.selectFrom(instructorCenter)
                .join(instructorCenter.center, center)
                .join(instructorCenter.instructor, instructor)
                .where(center.id.eq(centerId))
                .orderBy(createOrderSpecifier(sort))
                .fetch();
    }

    private OrderSpecifier[] createOrderSpecifier(int sortId) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (sortId == 0) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, instructorCenter.createdAt));
        } else {
            orderSpecifiers.add(new OrderSpecifier(Order.ASC, instructor.name));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
