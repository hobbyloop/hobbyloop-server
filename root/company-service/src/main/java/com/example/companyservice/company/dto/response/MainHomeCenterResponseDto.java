package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Center;
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

    private boolean isRefundable;

    private boolean isBookmark;

    public static MainHomeCenterResponseDto of(Center center, boolean isBookmark) {
        return MainHomeCenterResponseDto.builder()
                .centerId(center.getId())
                .centerName(center.getCenterName())
                .logoImageUrl(center.getLogoImageUrl())
                .address(center.getAddress())
                .isRefundable(center.getCompany().getIsRefundable())
                .isBookmark(isBookmark)
                .build();
    }
}
