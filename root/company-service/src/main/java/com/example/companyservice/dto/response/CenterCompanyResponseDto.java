package com.example.companyservice.dto.response;

import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.dto.response.CompanyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterCompanyResponseDto {

    private CenterCreateResponseDto centerInfo;

    private String logoImageUrl;

    private List<String> centerImageUrlList;

    private CompanyResponseDto companyInfo;

    public static CenterCompanyResponseDto of(CenterCreateResponseDto centerInfo,
                                              String logoImageUrl,
                                              List<String> centerImageUrlList,
                                              CompanyResponseDto companyInfo) {
        return CenterCompanyResponseDto.builder()
                .centerInfo(centerInfo)
                .logoImageUrl(logoImageUrl)
                .centerImageUrlList(centerImageUrlList)
                .companyInfo(companyInfo)
                .build();
    }
}
