package com.example.companyservice.company.repository.company;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.companyservice.company.entity.QCompany.company;

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CompanyApplyResponseDto> getCompanyApplyInfo() {
        return queryFactory
                .select(Projections.constructor(CompanyApplyResponseDto.class,
                        company.id,
                        company.createStatus,
                        company.representativeName,
                        company.businessNumber,
                        company.businessAddress)
                )
                .from(company)
                .orderBy(company.createdAt.desc())
                .fetch();
    }
}
