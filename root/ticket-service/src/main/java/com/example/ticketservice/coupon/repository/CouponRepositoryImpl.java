package com.example.ticketservice.coupon.repository;

import com.example.ticketservice.coupon.entity.CouponUsableScope;
import com.example.ticketservice.coupon.entity.QCoupon;
import com.example.ticketservice.coupon.repository.projection.CouponProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.ticketservice.coupon.entity.QCoupon.coupon;
import static com.example.ticketservice.coupon.entity.QMemberCoupon.memberCoupon;

@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CouponProjection> findCenterCouponList(Long memberId, Long companyId, Long centerId) {
        LocalDateTime now = LocalDateTime.now();

        List<CouponProjection> generalCoupons = queryFactory
                .select(Projections.constructor(CouponProjection.class, coupon, issuedCouponCount(coupon, memberId)))
                .from(coupon)
                .where(
                        coupon.usableScope.eq(CouponUsableScope.GENERAL.getValue()),
                        coupon.startDateTime.loe(now),
                        coupon.endDateTime.goe(now)
                )
                .fetch();

        List<CouponProjection> companyCoupons = companyId != null ? queryFactory
                .select(Projections.constructor(CouponProjection.class, coupon, issuedCouponCount(coupon, memberId)))
                .from(coupon)
                .where(
                        coupon.usableScope.eq(CouponUsableScope.SPECIFIC_COMPANY.getValue()),
                        coupon.startDateTime.loe(now),
                        coupon.endDateTime.goe(now),
                        coupon.companyId.eq(companyId)
                )
                .fetch() : Collections.emptyList();

        List<CouponProjection> centerCoupons = centerId != null ? queryFactory
                .select(Projections.constructor(CouponProjection.class, coupon, issuedCouponCount(coupon, memberId)))
                .from(coupon)
                .where(
                        coupon.usableScope.eq(CouponUsableScope.SPECIFIC_CENTER.getValue()),
                        coupon.startDateTime.loe(now),
                        coupon.endDateTime.goe(now),
                        coupon.centerId.eq(centerId)
                )
                .fetch() : Collections.emptyList();

        return Stream.of(generalCoupons, companyCoupons, centerCoupons)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private JPQLQuery<Long> issuedCouponCount(QCoupon coupon, Long memberId) {
        return JPAExpressions
                .select(memberCoupon.count())
                .from(memberCoupon)
                .where(memberCoupon.coupon.eq(coupon), memberCoupon.memberId.eq(memberId));
    }
}
