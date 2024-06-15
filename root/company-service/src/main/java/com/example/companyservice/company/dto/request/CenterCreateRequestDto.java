package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterCreateRequestDto {

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

    private List<HourRequestDto> operatingHourList;

    private List<HourRequestDto> breakHourList;

    @NotBlank
    private String representativeName;

    @NotBlank
    private String businessNumber;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate openingDate;

    private String onlineReportNumber;

    @NotBlank
    private String accountBank;

    @NotBlank
    private String accountNumber;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
