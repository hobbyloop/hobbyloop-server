package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponseDto {

    private String representativeName;

    private LocalDate openingDate;

    private String businessNumber;

    private String onlineReportNumber;

    public static CompanyResponseDto from(Company company) {
        return CompanyResponseDto.builder()
                .representativeName(company.getRepresentativeName())
                .openingDate(company.getOpeningDate())
                .businessNumber(company.getBusinessNumber())
                .onlineReportNumber(company.getOnlineReportNumber())
                .build();
    }
}
