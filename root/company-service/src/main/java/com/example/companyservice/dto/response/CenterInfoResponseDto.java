package com.example.companyservice.dto.response;

import com.example.companyservice.dto.response.HourResponseDto;
import com.example.companyservice.entity.Center;
import com.fasterxml.jackson.annotation.JsonProperty;
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
                .build();
    }
}
