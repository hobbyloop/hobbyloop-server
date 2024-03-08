package com.example.payservice.service;

import com.example.payservice.dto.request.CompanyRatePlanRequestDto;
import com.example.payservice.entity.CompanyRatePlan;
import com.example.payservice.repository.CompanyRatePlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyRatePlanServiceImpl implements CompanyRatePlanService {

    private final CompanyRatePlanRepository companyRatePlanRepository;

    @Override
    @Transactional
    public Long createCompanyRatePlan(CompanyRatePlanRequestDto requestDto) {
        CompanyRatePlan companyRatePlan = CompanyRatePlan.from(requestDto);
        return companyRatePlanRepository.save(companyRatePlan).getId();
    }
}
