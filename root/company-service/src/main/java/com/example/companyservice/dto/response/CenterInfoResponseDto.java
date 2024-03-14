package com.example.companyservice.dto.response;

import com.example.companyservice.dto.response.HourResponseDto;
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
public class CenterInfoResponseDto {

    public String centerName;

    public String address;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;

    public static CenterInfoResponseDto of(Center center,
                                           List<HourResponseDto> operatingHourList,
                                           List<HourResponseDto> breakHourList) {
        return CenterInfoResponseDto.builder()
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .build();
    }
}
