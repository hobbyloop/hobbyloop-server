package com.example.companyservice.company.repository.company;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<CompanyApplyResponseDto> getCompanyApplyInfo(Pageable pageable);
}
