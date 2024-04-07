package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementResponseDto {

    private Long advertisementId;

    private Long centerId;

    private String content;

    private String keyword;

    private String bannerImageUrl;

    public static AdvertisementResponseDto from(Advertisement advertisement) {
        return AdvertisementResponseDto.builder()
                .advertisementId(advertisement.getId())
                .centerId(advertisement.getCenter().getId())
                .content(advertisement.getContent())
                .keyword(advertisement.getKeyword())
                .bannerImageUrl(advertisement.getBannerImage())
                .build();
    }
}
