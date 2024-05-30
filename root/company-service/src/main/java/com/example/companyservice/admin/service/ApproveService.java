package com.example.companyservice.admin.service;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;

import java.util.List;

public interface ApproveService {
    List<CompanyApplyResponseDto> getCompanyApplyInfo();

    Long respondCompany(long companyId, String answer);
}
