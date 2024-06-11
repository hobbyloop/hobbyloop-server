package com.example.companyservice.company.repository.company;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.companyservice.company.entity.QCompany.company;

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CompanyApplyResponseDto> getCompanyApplyInfo(Pageable pageable) {
        List<CompanyApplyResponseDto> result = queryFactory
                .select(Projections.constructor(CompanyApplyResponseDto.class,
                        company.id,
                        company.createStatus,
                        company.representativeName,
                        company.businessNumber,
                        company.businessAddress)
                )
                .from(company)
                .orderBy(company.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(company.count())
                .from(company);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
