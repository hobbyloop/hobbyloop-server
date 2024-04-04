package com.example.ticketservice.client.dto.response;

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
}
