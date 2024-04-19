package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.client.dto.response.TicketDetailClientResponseDto;
import com.example.companyservice.company.entity.Center;
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

    private String logoImageUrl;

    private List<String> centerImageUrlList;

    private boolean isBookmark;

    private boolean isLooppass;

    private boolean isRefundable;

    private String contact;

    private String kakaoLink;

    private String address;

    private List<HourResponseDto> operatingHourList;

    private List<HourResponseDto> breakHourList;

    private float score;

    private int reviewCount;

    private int price;

    private int discountRate;

    private int calculatedPrice;

    private String announcement;

    private String introduce;

    private String representativeName;

    private LocalDate openingDate;

    private String businessNumber;

    private String onlineReportNumber;

    public static CenterInfoDetailResponseDto of(Center center,
                                                 List<String> centerImageUrlList,
                                                 boolean isBookmark,
                                                 List<HourResponseDto> operatingHourList,
                                                 List<HourResponseDto> breakHourList,
                                                 TicketDetailClientResponseDto ticketInfo) {
        return CenterInfoDetailResponseDto.builder()
                .centerName(center.getCenterName())
                .logoImageUrl(center.getLogoImageUrl())
                .centerImageUrlList(centerImageUrlList)
                .isBookmark(isBookmark)
                .isLooppass(center.getCompany().getIsLooppass())
                .isRefundable(center.getCompany().getIsRefundable())
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .address(center.getAddress())
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .score(ticketInfo != null ? ticketInfo.getScore() : 0)
                .reviewCount(ticketInfo != null ? ticketInfo.getReviewCount() : 0)
                .price(ticketInfo != null ? ticketInfo.getPrice() : 0)
                .discountRate(ticketInfo != null ? ticketInfo.getDiscountRate() : 0)
                .calculatedPrice(ticketInfo != null ? ticketInfo.getCalculatedPrice() : 0)
                .announcement(center.getAnnouncement())
                .introduce(center.getIntroduce())
                .representativeName(center.getRepresentativeName())
                .openingDate(center.getOpeningDate())
                .businessNumber(center.getBusinessNumber())
                .onlineReportNumber(center.getOnlineReportNumber())
                .build();
    }
}
