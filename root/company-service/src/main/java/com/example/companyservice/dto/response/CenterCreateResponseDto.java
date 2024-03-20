package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterCreateResponseDto {

    private Long centerId;

    private String centerName;

    private String address;

    private String contact;

    private String kakaoLink;

    private String logoImageUrl;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;

    public static CenterCreateResponseDto of(Center center,
                                             String logoImageUrl,
                                             List<HourResponseDto> operatingHourList,
                                             List<HourResponseDto> breakHourList) {
        return CenterCreateResponseDto.builder()
                .centerId(center.getId())
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .logoImageUrl(logoImageUrl)
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .build();
    }
}
