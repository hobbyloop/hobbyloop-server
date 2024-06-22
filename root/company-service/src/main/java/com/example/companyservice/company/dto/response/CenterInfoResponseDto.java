package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterInfoResponseDto {

    public String centerName;

    public String address;

    private String announcement;

    private String introduce;

    private String contact;

    private String kakaoLink;

    private boolean isLooppass;

    private boolean isRefundable;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;

    private double latitude;

    private double longitude;

    public static CenterInfoResponseDto of(Center center,
                                           List<HourResponseDto> operatingHourList,
                                           List<HourResponseDto> breakHourList) {
        return CenterInfoResponseDto.builder()
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .announcement(center.getAnnouncement())
                .introduce(center.getIntroduce())
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .isLooppass(center.getCompany().getIsLooppass())
                .isRefundable(center.getCompany().getIsRefundable())
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .latitude(center.getLatitude())
                .longitude(center.getLongitude())
                .build();
    }
}
