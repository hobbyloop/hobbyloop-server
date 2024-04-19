package com.example.companyservice.company.service;

import com.example.companyservice.company.dto.request.CompanyUpdateRequestDto;

public interface CompanyService {

    Long updateCompanyInfo(long companyId, CompanyUpdateRequestDto requestDto);

    Boolean checkTaxFree(long companyId);
}
