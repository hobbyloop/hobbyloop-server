package com.example.companyservice.company.service;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.company.client.TicketServiceClient;
import com.example.companyservice.company.client.dto.request.CompanyRatePlanRequestDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;

    private final TicketServiceClient ticketServiceClient;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public TokenResponseDto createCompany(CompanyCreateRequestDto requestDto) {
        CompanyRatePlanRequestDto companyRatePlanRequestDto = CompanyRatePlanRequestDto.from(requestDto);
        BaseResponseDto<Long> companyRatePlan = ticketServiceClient.createCompanyRatePlan(companyRatePlanRequestDto);
        Company company = Company.of(requestDto, companyRatePlan.getData());
        companyRepository.save(company);
        String accessToken = jwtUtils.createToken(company.getId(), company.getRole());
        String refreshToken = jwtUtils.createRefreshToken(company.getId(), company.getRole());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkTaxFree(long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        return company.getIsDutyFree();
    }
}
