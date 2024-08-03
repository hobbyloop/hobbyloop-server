package com.example.companyservice.company.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.companyservice.company.dto.request.BusinessRequestDto;
import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Center extends TimeStamped {

    @Id
    @Column(name = "center_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String centerName;

    private String address;

    private String announcement;

    private String introduce;

    private String contact;

    private String kakaoLink;

    private String representativeName;

    private LocalDate openingDate;

    private String businessNumber;

    private String onlineReportNumber;

    private int adPoint;

    private int paymentBalance;

    private int adBalance;

    private String logoImageKey;

    private String logoImageUrl;

    private double latitude;

    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public static Center of(CenterCreateRequestDto requestDto, Company company, String logoImageKey, String logoImageUrl) {
        return Center.builder()
                .centerName(requestDto.getCenterName())
                .address(requestDto.getAddress())
                .announcement(requestDto.getAnnouncement())
                .introduce(requestDto.getIntroduce())
                .contact(requestDto.getContact())
                .kakaoLink(requestDto.getKakaoLink())
                .representativeName(requestDto.getRepresentativeName())
                .openingDate(requestDto.getOpeningDate())
                .businessNumber(requestDto.getBusinessNumber())
                .onlineReportNumber(requestDto.getOnlineReportNumber())
                .logoImageKey(logoImageKey)
                .logoImageUrl(logoImageUrl)
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .company(company)
                .build();
    }

    public void centerUpdate(CenterUpdateRequestDto requestDto, String logoImageKey, String logoImageUrl) {
        this.centerName = requestDto.getCenterName();
        this.address = requestDto.getAddress();
        this.announcement = requestDto.getAnnouncement();
        this.introduce = requestDto.getIntroduce();
        this.contact = requestDto.getContact();
        this.kakaoLink = requestDto.getKakaoLink();
        this.logoImageKey = logoImageKey;
        this.logoImageUrl = logoImageUrl;
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }

    public void businessInfoUpdate(BusinessRequestDto requestDto) {
        this.representativeName = requestDto.getRepresentativeName();
        this.businessNumber = requestDto.getBusinessNumber();
        this.openingDate = requestDto.getOpeningDate();
        this.onlineReportNumber = requestDto.getOnlineReportNumber();
    }

    public void rollbackUpdate(CenterOriginalAndUpdateInfoDto requestDto) {
        this.centerName = requestDto.getOriginalCenterName();
        this.logoImageKey = requestDto.getOriginalLogoImageKey();
        this.logoImageUrl = requestDto.getOriginalLogoImageUrl();
        this.address = requestDto.getOriginalAddress();
        this.announcement = requestDto.getAnnouncement();
        this.introduce = requestDto.getIntroduce();
        this.contact = requestDto.getContact();
        this.kakaoLink = requestDto.getKakaoLink();
        this.latitude = requestDto.getOriginalLatitude();
        this.longitude = requestDto.getOriginalLongitude();
    }
}
