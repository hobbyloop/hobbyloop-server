package com.example.companyservice.company.entity;

import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Company extends TimeStamped {

    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String provider;

    private String subject;

    private String oauth2AccessToken;

    private String ci;

    private String di;

    private Role role;

    private Boolean isDutyFree;

    private String companyName;

    private String representativeName;

    private String phoneNumber;

    private String businessNumber;

    private String businessAddress;

    private LocalDate openingDate;

    private LocalDate startAt;

    private LocalDate endAt;

    private String onlineReportNumber;

    private String accountBank;

    private String accountNumber;

    private Boolean isLooppass;

    private Boolean isRefundable;

    private int createStatus;

    private Boolean isOption1;

    private Boolean isOption2;

    private double latitude;

    private double longitude;

    private boolean isDelete;

    private Long companyLatePlanId;

    public static Company of(CompanyCreateRequestDto requestDto, Long companyLatePlanId) {
        return Company.builder()
                .email(requestDto.getEmail())
                .provider(requestDto.getProvider())
                .subject(requestDto.getSubject())
                .oauth2AccessToken(requestDto.getOauth2AccessToken())
                .ci(requestDto.getCi())
                .di(requestDto.getDi())
                .role(Role.COMPANY)
                .isOption1(requestDto.isOption1())
                .isOption2(requestDto.isOption2())
                .isDutyFree(requestDto.isDutyFree())
                .companyName(requestDto.getCompanyName())
                .representativeName(requestDto.getRepresentativeName())
                .phoneNumber(requestDto.getPhoneNumber())
                .businessNumber(requestDto.getBusinessNumber())
                .businessAddress(requestDto.getBusinessAddress())
                .openingDate(requestDto.getOpeningDate())
                .onlineReportNumber(requestDto.getOnlineReportNumber())
                .accountBank(requestDto.getAccountBank())
                .accountNumber(requestDto.getAccountNumber())
                .isLooppass(requestDto.isLooppass())
                .isRefundable(requestDto.isRefundable())
                .companyLatePlanId(companyLatePlanId)
                .createStatus(CreateStatusEnum.WAIT.getTypeValue())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .isDelete(false)
                .build();
    }

    public void updateCreateStatus(int status) {
        this.createStatus = status;
    }

    public void deleteCompany() {
        this.isDelete = true;
    }
}
