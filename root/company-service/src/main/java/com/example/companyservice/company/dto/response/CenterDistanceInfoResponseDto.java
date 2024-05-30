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
public class CenterDistanceInfoResponseDto {

    private Long centerId;

    private boolean isRefundable;

    private boolean isBookmark;

    private String logoImageUrl;

    private String centerName;

    private String address;

    private boolean isSatisfied;

    public static CenterDistanceInfoResponseDto of(Center center, boolean isBookmark, boolean isSatisfied) {
        return CenterDistanceInfoResponseDto.builder()
                .centerId(center.getId())
                .isRefundable(center.getCompany().getIsRefundable())
                .isBookmark(isBookmark)
                .logoImageUrl(center.getLogoImageUrl())
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .isSatisfied(isSatisfied)
                .build();
    }
}
