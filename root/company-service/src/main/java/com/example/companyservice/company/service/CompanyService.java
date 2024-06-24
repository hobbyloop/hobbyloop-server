package com.example.companyservice.company.service;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;

public interface CompanyService {

    TokenResponseDto createCompany(CompanyCreateRequestDto requestDto);

    Boolean checkTaxFree(long companyId);

    String getCompanyName(long companyId);
}
