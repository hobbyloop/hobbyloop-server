package com.example.payservice.service;

import com.example.payservice.dto.request.CompanyRatePlanRequestDto;

public interface CompanyRatePlanService {
    Long createCompanyRatePlan(CompanyRatePlanRequestDto requestDto);
}
