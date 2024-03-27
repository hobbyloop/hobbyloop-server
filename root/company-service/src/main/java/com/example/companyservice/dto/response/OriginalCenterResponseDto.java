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
public class OriginalCenterResponseDto {

    private String centerName;

    private String address;

    private String logoImageUrl;

    private List<String> centerImageUrl;

    private String announcement;

    private String introduce;

    private String contact;

    private String kakaoLink;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;

    public static OriginalCenterResponseDto of(Center center,
                                               List<String> centerImageUrl,
                                               List<HourResponseDto> operatingHourList,
                                               List<HourResponseDto> breakHourList) {
        return OriginalCenterResponseDto.builder()
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .logoImageUrl(center.getLogoImageUrl())
                .centerImageUrl(centerImageUrl)
                .announcement(center.getAnnouncement())
                .introduce(center.getIntroduce())
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .build();
    }
}
