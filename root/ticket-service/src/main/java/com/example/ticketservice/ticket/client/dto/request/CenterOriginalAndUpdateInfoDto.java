package com.example.ticketservice.ticket.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterOriginalAndUpdateInfoDto {

    private Long centerId;

    private String originalCenterName;

    private String originalLogoImageKey;

    private String originalLogoImageUrl;

    private String originalAddress;

    private String announcement;

    private String introduce;

    private String contact;

    private String kakaoLink;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;

    private Double originalLatitude;

    private Double originalLongitude;

    private String newCenterName;

    private String newLogoImageKey;

    private String newLogoImageUrl;

    private String newAddress;

    private Double newLatitude;

    private Double newLongitude;

    private List<String> oldCenterImageKeyList;

    private List<String> newCenterImageKeyList;
}
