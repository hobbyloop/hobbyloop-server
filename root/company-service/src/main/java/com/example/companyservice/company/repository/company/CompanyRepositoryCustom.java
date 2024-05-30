package com.example.companyservice.company.repository.company;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;

import java.util.List;

public interface CompanyRepositoryCustom {
    List<CompanyApplyResponseDto> getCompanyApplyInfo();
}
