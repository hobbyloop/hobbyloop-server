package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Advertisement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "배너 광고 조회 응답 바디")
public class AdvertisementResponseDto {

    @Schema(description = "광고 아이디", example = "1")
    private Long advertisementId;

    @Schema(description = "시설 아이디", example = "1")
    private Long centerId;

    @Schema(description = "광고 캠페인명")
    private String campaignName;

    @Schema(description = "시설 이름")
    private String centerName;

    @Schema(description = "광고 문구")
    private String content;

    @Schema(description = "키워드")
    private String keyword;

    @Schema(description = "배너 이미지 url")
    private String bannerImageUrl;

    @Schema(description = "광고 게재 시작 날짜")
    private LocalDate adStart;

    @Schema(description = "광고 게재 종료 날짜")
    private LocalDate adEnd;

    @Schema(description = "광고 게재 순위")
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
