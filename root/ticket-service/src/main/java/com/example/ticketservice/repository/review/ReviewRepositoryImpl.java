package com.example.ticketservice.repository.review;

import com.example.ticketservice.entity.Review;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.ticketservice.entity.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> getReviewList(long ticketId, long reviewId) {
        return queryFactory
                .selectFrom(review)
                .where(review.ticket.id.eq(ticketId).and(ltReviewId(reviewId)))
                .limit(20)
                .orderBy(review.id.desc())
                .fetch();
    }

    @Override
    public List<Review> getReviewListSorting(long ticketId, int pageNo, int sortId) {
        return queryFactory
                .selectFrom(review)
                .where(review.ticket.id.eq(ticketId))
                .limit(20)
                .offset(pageNo * 20)
                .orderBy(createOrderSpecifier(sortId))
                .fetch();
    }

    private BooleanExpression ltReviewId(long reviewId) {
        return reviewId != -1 ? review.id.lt(reviewId) : null;
    }

    private OrderSpecifier[] createOrderSpecifier(int sortId) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (sortId == 0) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, review.score));
        } else if (sortId == 1) {
            orderSpecifiers.add(new OrderSpecifier(Order.ASC, review.score));
        } else if (sortId == 2) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, review.id));
        } else {
            orderSpecifiers.add(new OrderSpecifier(Order.ASC, review.id));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
