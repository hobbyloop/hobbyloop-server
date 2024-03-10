package com.example.companyservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class CenterCreateRequestDto {

    private String centerName;

    private String address;

    private String announcement;

    private String introduce;

    private String contact;

    private String kakaoLink;

    private List<HourRequestDto> operatingHourList;

    private List<HourRequestDto> breakHourList;

    private String representativeName;

    private String businessNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate openingDate;

    private String onlineReportNumber;
}
