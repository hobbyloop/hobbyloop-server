package com.example.companyservice.company.repository.bookmark;

import com.example.companyservice.company.entity.Bookmark;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.companyservice.company.entity.QBookmark.bookmark;

@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Bookmark> getBookmarkList(long memberId, long bookmarkId, int sortId) {
        return queryFactory
                .selectFrom(bookmark)
                .where(bookmark.memberId.eq(memberId)
                        .and(ltBookmarkId(bookmarkId, sortId)))
                .limit(20)
                .orderBy(createOrderSpecifier(sortId))
                .fetch();
    }

    private BooleanExpression ltBookmarkId(long bookmarkId, long sortId) {
        return bookmarkId != -1 ? sortId == 0 ? bookmark.id.lt(bookmarkId) : bookmark.id.gt(bookmarkId) : null;
    }

    private OrderSpecifier[] createOrderSpecifier(int sortId) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (sortId == 0) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, bookmark.id));
        } else {
            orderSpecifiers.add(new OrderSpecifier(Order.ASC, bookmark.id));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
