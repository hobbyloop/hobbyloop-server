package com.example.companyservice.admin.service;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.entity.CreateStatusEnum;
import com.example.companyservice.company.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApproveServiceImpl implements ApproveService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyApplyResponseDto> getCompanyApplyInfo(Pageable pageable) {
        return companyRepository.getCompanyApplyInfo(pageable);
    }

    @Override
    @Transactional
    public Long respondCompany(long companyId, String answer) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));

        CreateStatusEnum status = CreateStatusEnum.findByValue(company.getCreateStatus());
        if (CreateStatusEnum.SUCCESS == status) {
            throw new ApiException(ExceptionEnum.ALREADY_ACCEPT_COMPANY_EXCEPTION);
        } else if (CreateStatusEnum.REJECT == status) {
            throw new ApiException(ExceptionEnum.ALREADY_REJECT_COMPANY_EXCEPTION);
        }

        company.updateCreateStatus(CreateStatusEnum.findByName(answer).getTypeValue());
        return company.getId();
    }
}
