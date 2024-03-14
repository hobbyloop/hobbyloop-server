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
public class CenterInfoResponseDto {

    public String centerName;

    public String address;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;
}
