package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementResponseDto {

    private Long advertisementId;

    private Long centerId;

    private String campaignName;

    private String centerName;

    private String content;

    private String keyword;

    private String bannerImageUrl;

    private LocalDate adStart;

    private LocalDate adEnd;

    private int adRank;

    public static AdvertisementResponseDto from(Advertisement advertisement) {
        return AdvertisementResponseDto.builder()
                .advertisementId(advertisement.getId())
                .centerId(advertisement.getCenter().getId())
                .campaignName(advertisement.getCampaignName())
                .centerName(advertisement.getCenter().getCenterName())
                .content(advertisement.getContent())
                .keyword(advertisement.getKeyword())
                .bannerImageUrl(advertisement.getBannerImageUrl())
                .adStart(advertisement.getAdStart())
                .adEnd(advertisement.getAdEnd())
                .adRank(advertisement.getAdRank())
                .build();
    }
}
