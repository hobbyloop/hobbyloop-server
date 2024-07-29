package com.example.companyservice.company.repository.advertisement;

import com.example.companyservice.company.entity.AdvertisementLog;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.example.companyservice.company.entity.QAdvertisement.advertisement;
import static com.example.companyservice.company.entity.QAdvertisementLog.advertisementLog;
import static com.example.companyservice.company.entity.QCenter.center;

@RequiredArgsConstructor
public class AdvertisementLogRepositoryImpl implements AdvertisementLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AdvertisementLog> findAllByCenterIdBetweenYear(long centerId, int year) {
        return queryFactory
                .selectFrom(advertisementLog)
                .join(advertisementLog.advertisement, advertisement)
                .join(advertisement.center, center)
                .where(center.id.eq(centerId)
                        .and(betweenYear(year)))
                .fetch();
    }

    private BooleanExpression betweenYear(int year) {
        LocalDateTime fromDateTime = LocalDateTime.of(year, Month.JANUARY, 0, 0, 0, 0);
        LocalDateTime toDateTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        return advertisementLog.createdAt.between(fromDateTime, toDateTime);
    }
}
