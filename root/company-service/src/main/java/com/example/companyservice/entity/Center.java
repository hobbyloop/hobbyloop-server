package com.example.companyservice.entity;

import com.example.companyservice.dto.request.BusinessRequestDto;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.request.CenterUpdateRequestDto;
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

    private boolean isDelete;

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
                .isDelete(false)
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
    }

    public void businessInfoUpdate(BusinessRequestDto requestDto) {
        this.representativeName = requestDto.getRepresentativeName();
        this.businessNumber = requestDto.getBusinessNumber();
        this.openingDate = requestDto.getOpeningDate();
        this.onlineReportNumber = requestDto.getOnlineReportNumber();
    }
}
