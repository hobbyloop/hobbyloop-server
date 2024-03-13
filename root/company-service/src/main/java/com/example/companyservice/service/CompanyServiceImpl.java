package com.example.companyservice.service;

import com.example.companyservice.client.PayServiceClient;
import com.example.companyservice.client.dto.CompanyRatePlanRequestDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.request.CompanyUpdateRequestDto;
import com.example.companyservice.entity.Company;
import com.example.companyservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;

    private final PayServiceClient payServiceClient;

    @Override
    @Transactional
    public Long updateCompanyInfo(long companyId, CompanyUpdateRequestDto requestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        CompanyRatePlanRequestDto companyRatePlanRequestDto = CompanyRatePlanRequestDto.from(requestDto);
        BaseResponseDto<Long> companyRatePlan = payServiceClient.createCompanyRatePlan(companyRatePlanRequestDto);
        company.updateCompany(requestDto, companyRatePlan.getData());
        return company.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkTaxFree(long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        return company.getIsDutyFree();
    }
}
