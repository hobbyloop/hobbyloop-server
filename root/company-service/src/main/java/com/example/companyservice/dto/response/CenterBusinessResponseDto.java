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

    private List<String> centerImageUrlList;

    private BusinessResponseDto businessmanInfo;

    public static CenterBusinessResponseDto of(CenterCreateResponseDto centerInfo,
                                               List<String> centerImageUrlList,
                                               BusinessResponseDto businessmanInfo) {
        return CenterBusinessResponseDto.builder()
                .centerInfo(centerInfo)
                .centerImageUrlList(centerImageUrlList)
                .businessmanInfo(businessmanInfo)
                .build();
    }
}
