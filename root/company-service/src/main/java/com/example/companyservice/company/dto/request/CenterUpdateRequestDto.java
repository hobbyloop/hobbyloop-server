package com.example.companyservice.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CenterUpdateRequestDto {

    @NotBlank
    private String centerName;

    @NotBlank
    private String address;

    private String announcement;

    private String introduce;

    @NotBlank
    private String contact;

    @NotBlank
    private String kakaoLink;

    @NotBlank
    private List<HourRequestDto> operatingHourList;

    private List<HourRequestDto> breakHourList;

    @NotBlank
    private double latitude;

    @NotBlank
    private double longitude;
}
