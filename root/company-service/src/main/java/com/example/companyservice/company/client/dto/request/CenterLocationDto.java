package com.example.companyservice.company.client.dto.request;

import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterLocationDto {

    private long centerId;

    private String centerName;

    private String logoImageUrl;

    private String address;

    private double latitude;

    private double longitude;

    public static CenterLocationDto of(long centerId, CenterUpdateRequestDto requestDto, String logoImageUrl) {
        return CenterLocationDto.builder()
                .centerId(centerId)
                .centerName(requestDto.getCenterName())
                .logoImageUrl(logoImageUrl)
                .address(requestDto.getAddress())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .build();
    }
}
