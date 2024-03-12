package com.example.companyservice.dto.response;

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

    private CenterBusinessResponseDto businessmanInfo;

    public static CenterCompanyResponseDto of(CenterCreateResponseDto centerInfo,
                                              String logoImageUrl,
                                              List<String> centerImageUrlList,
                                              CenterBusinessResponseDto businessmanInfo) {
        return CenterCompanyResponseDto.builder()
                .centerInfo(centerInfo)
                .logoImageUrl(logoImageUrl)
                .centerImageUrlList(centerImageUrlList)
                .businessmanInfo(businessmanInfo)
                .build();
    }
}
