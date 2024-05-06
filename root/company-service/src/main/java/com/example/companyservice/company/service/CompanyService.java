package com.example.companyservice.company.service;

import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;

public interface CompanyService {

    Long createCompany(CompanyCreateRequestDto requestDto);

    Boolean checkTaxFree(long companyId);
}
