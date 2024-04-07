package com.example.companyservice.repository.advertisement;

import com.example.companyservice.dto.request.LocationRequestDto;
import com.example.companyservice.entity.Advertisement;
import com.example.companyservice.entity.AdvertisementTypeEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.companyservice.entity.QAdvertisement.advertisement;
import static com.example.companyservice.entity.QCenter.center;

@RequiredArgsConstructor
public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Advertisement> findAllAdvertisementAroundLocation() {
        return queryFactory
                .selectFrom(advertisement)
                .join(advertisement.center, center)
                .where(advertisement.isOpen.eq(true)
                        .and(advertisement.adType.eq(AdvertisementTypeEnum.BANNER.getTypeValue()))
                        .and(betweenDate()))
                .orderBy(advertisement.adRank.desc())
                .fetch();
    }

    private BooleanExpression betweenDate() {
        return advertisement.adStart.loe(LocalDate.now()).and(advertisement.adEnd.goe(LocalDate.now()));
    }
}
