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
public class CenterBusinessResponseDto {

    private CenterCreateResponseDto centerInfo;

    private String logoImageUrl;

    private List<String> centerImageUrlList;

    private BusinessResponseDto businessmanInfo;

    public static CenterBusinessResponseDto of(CenterCreateResponseDto centerInfo,
                                               String logoImageUrl,
                                               List<String> centerImageUrlList,
                                               BusinessResponseDto businessmanInfo) {
        return CenterBusinessResponseDto.builder()
                .centerInfo(centerInfo)
                .logoImageUrl(logoImageUrl)
                .centerImageUrlList(centerImageUrlList)
                .businessmanInfo(businessmanInfo)
                .build();
    }
}
