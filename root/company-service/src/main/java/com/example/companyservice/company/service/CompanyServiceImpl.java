package com.example.companyservice.company.service;

import com.example.companyservice.company.client.TicketServiceClient;
import com.example.companyservice.company.client.dto.request.CompanyRatePlanRequestDto;
import com.example.companyservice.company.common.exception.ApiException;
import com.example.companyservice.company.common.exception.ExceptionEnum;
import com.example.companyservice.company.dto.request.CompanyUpdateRequestDto;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;

    private final TicketServiceClient ticketServiceClient;

    @Override
    @Transactional
    public Long updateCompanyInfo(long companyId, CompanyUpdateRequestDto requestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        CompanyRatePlanRequestDto companyRatePlanRequestDto = CompanyRatePlanRequestDto.from(requestDto);
        BaseResponseDto<Long> companyRatePlan = ticketServiceClient.createCompanyRatePlan(companyRatePlanRequestDto);
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
