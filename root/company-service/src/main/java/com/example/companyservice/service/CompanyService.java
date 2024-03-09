package com.example.companyservice.service;

import com.example.companyservice.dto.request.CompanyUpdateRequestDto;

public interface CompanyService {

    Long updateCompanyInfo(long companyId, CompanyUpdateRequestDto requestDto);
}
