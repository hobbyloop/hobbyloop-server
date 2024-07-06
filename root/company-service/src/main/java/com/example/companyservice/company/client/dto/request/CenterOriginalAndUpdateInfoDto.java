package com.example.companyservice.company.client.dto.request;

import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.company.dto.response.HourResponseDto;
import com.example.companyservice.company.entity.Center;
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

    public void setOldCenterImageKeyList(List<String> oldCenterImageKeyList) {
        this.oldCenterImageKeyList = oldCenterImageKeyList;
    }

    public void setNewCenterImageKeyList(List<String> newCenterImageKeyList) {
        this.newCenterImageKeyList = newCenterImageKeyList;
    }

    public static CenterOriginalAndUpdateInfoDto of(Center center,
                                                    List<HourResponseDto> operatingHourList,
                                                    List<HourResponseDto> breakHourList,
                                                    CenterUpdateRequestDto requestDto,
                                                    String newLogoImageKey,
                                                    String newLogoImageUrl) {
        return CenterOriginalAndUpdateInfoDto.builder()
                .centerId(center.getId())
                .originalCenterName(center.getCenterName())
                .originalLogoImageKey(center.getLogoImageKey())
                .originalLogoImageUrl(center.getLogoImageUrl())
                .originalAddress(center.getAddress())
                .announcement(center.getAnnouncement())
                .introduce(center.getIntroduce())
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .originalLatitude(center.getLatitude())
                .originalLongitude(center.getLongitude())
                .newCenterName(requestDto.getCenterName())
                .newLogoImageKey(newLogoImageKey)
                .newLogoImageUrl(newLogoImageUrl)
                .newAddress(requestDto.getAddress())
                .newLatitude(requestDto.getLatitude())
                .newLongitude(requestDto.getLongitude())
                .build();
    }
}
