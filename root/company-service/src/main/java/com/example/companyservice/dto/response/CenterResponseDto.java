package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterResponseDto {

    private Long centerId;

    private String centerName;

    private String logoImageUrl;

    public static CenterResponseDto from(Center center) {
        return CenterResponseDto.builder()
                .centerId(center.getId())
                .centerName(center.getCenterName())
                .logoImageUrl(center.getLogoImageUrl())
                .build();
    }
}
