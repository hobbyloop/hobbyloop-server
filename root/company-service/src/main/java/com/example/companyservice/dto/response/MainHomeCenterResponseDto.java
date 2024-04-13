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
public class MainHomeCenterResponseDto {

    private Long centerId;

    private String centerName;

    private String logoImageUrl;

    private String address;

    private boolean isBookmark;

    public static MainHomeCenterResponseDto of(Center center, boolean isBookmark) {
        return MainHomeCenterResponseDto.builder()
                .centerId(center.getId())
                .centerName(center.getCenterName())
                .logoImageUrl(center.getLogoImageUrl())
                .address(center.getAddress())
                .isBookmark(isBookmark)
                .build();
    }
}
