package com.example.companyservice.admin.service;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApproveService {
    Page<CompanyApplyResponseDto> getCompanyApplyInfo(Pageable pageable);

    Long respondCompany(long companyId, String answer);
}
