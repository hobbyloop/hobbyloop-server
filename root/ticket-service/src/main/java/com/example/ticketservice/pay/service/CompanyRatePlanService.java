package com.example.ticketservice.pay.service;

import com.example.ticketservice.pay.dto.request.CompanyRatePlanRequestDto;

public interface CompanyRatePlanService {
    Long createCompanyRatePlan(CompanyRatePlanRequestDto requestDto);
}
