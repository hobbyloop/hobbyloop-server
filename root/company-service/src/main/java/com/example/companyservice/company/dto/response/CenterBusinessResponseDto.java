package com.example.companyservice.company.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "시설 관리자 페이지 시설 상세 응답 바디")
public class CenterBusinessResponseDto {

    @Schema(description = "시설 정보")
    private CenterCreateResponseDto centerInfo;

    @Schema(description = "시설 소개 이미지 리스트")
    private List<String> centerImageUrlList;

    @Schema(description = "사업자 정보")
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
