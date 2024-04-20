package com.example.companyservice.company.repository.advertisement;

import com.example.companyservice.company.entity.Advertisement;
import com.example.companyservice.company.entity.AdvertisementTypeEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.companyservice.company.entity.QAdvertisement.advertisement;
import static com.example.companyservice.company.entity.QCenter.center;

@RequiredArgsConstructor
public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Advertisement> findAllBannerAdvertisement() {
        return queryFactory
                .selectFrom(advertisement)
                .join(advertisement.center, center)
                .where(advertisement.isOpen.eq(true)
                        .and(advertisement.adType.eq(AdvertisementTypeEnum.BANNER.getTypeValue()))
                        .and(betweenDate()))
                .orderBy(advertisement.adRank.asc())
                .fetch();
    }

    @Override
    public List<Advertisement> findAllCPCAdvertisement() {
        return queryFactory
                .selectFrom(advertisement)
                .join(advertisement.center, center)
                .where(advertisement.isOpen.eq(true)
                        .and(advertisement.adType.eq(AdvertisementTypeEnum.CPC.getTypeValue()))
                        .and(betweenDate()))
                .orderBy(advertisement.price.desc())
                .fetch();
    }

    @Override
    public List<Advertisement> findAllCPCCPMAdvertisement() {
        return queryFactory
                .selectFrom(advertisement)
                .join(advertisement.center, center)
                .where(advertisement.isOpen.eq(true)
                        .and(advertisement.adType.eq(AdvertisementTypeEnum.CPC.getTypeValue()))
                        .or(advertisement.adType.eq(AdvertisementTypeEnum.CPM.getTypeValue()))
                        .and(betweenDate()))
                .orderBy(advertisement.price.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<Integer> getUsedRank() {
        return queryFactory
                .select(advertisement.adRank).distinct()
                .from(advertisement)
                .where(advertisement.adRank.isNotNull())
                .fetch();
    }

    private BooleanExpression betweenDate() {
        return advertisement.adStart.loe(LocalDate.now()).and(advertisement.adEnd.goe(LocalDate.now()));
    }
}
