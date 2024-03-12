package com.example.companyservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CenterUpdateRequestDto {

    private String centerName;

    private String address;

    private String announcement;

    private String introduce;

    private String contact;

    private String kakaoLink;

    private List<HourRequestDto> operatingHourList;

    private List<HourRequestDto> breakHourList;
}
