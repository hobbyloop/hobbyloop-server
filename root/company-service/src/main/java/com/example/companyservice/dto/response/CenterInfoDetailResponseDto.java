package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterInfoDetailResponseDto {

    private String centerName;

    private boolean isBookmark;

    private String contact;

    private String kakaoLink;

    private String address;

    private List<HourResponseDto> operatingHourList;

    private int reviewCount;

    private String announcement;

    private String introduce;

    private String representativeName;

    private LocalDate openingDate;

    private String businessNumber;

    private String onlineReportNumber;

    public static CenterInfoDetailResponseDto of(Center center,
                                                 boolean isBookmark,
                                                 List<HourResponseDto> operatingHourList,
                                                 int reviewCount) {
        return CenterInfoDetailResponseDto.builder()
                .centerName(center.getCenterName())
                .isBookmark(isBookmark)
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .address(center.getAddress())
                .operatingHourList(operatingHourList)
                .reviewCount(reviewCount)
                .announcement(center.getAnnouncement())
                .introduce(center.getIntroduce())
                .representativeName(center.getRepresentativeName())
                .openingDate(center.getOpeningDate())
                .businessNumber(center.getBusinessNumber())
                .onlineReportNumber(center.getOnlineReportNumber())
                .build();
    }
}
