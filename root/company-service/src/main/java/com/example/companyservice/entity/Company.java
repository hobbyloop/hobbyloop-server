package com.example.companyservice.entity;

import com.example.companyservice.dto.request.CompanyUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private String password;

    private String provider;

    private String providerId;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

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

    private Boolean isReservationService;

    private Boolean isRefundable;

    private int createStatus;

    private Boolean isOption1;

    private Boolean isOption2;

    private double latitude;

    private double longitude;

    private boolean isDelete;

    private Long companyLatePlanId;

    public static Company from(String email) {
        return Company.builder()
                .email(email)
                .isDelete(false)
                .build();
    }

    public void updateCompany(CompanyUpdateRequestDto requestDto, Long companyLatePlanId) {
        this.isOption1 = requestDto.isOption1();
        this.isOption2 = requestDto.isOption2();
        this.isDutyFree = requestDto.isDutyFree();
        this.companyName = requestDto.getCompanyName();
        this.representativeName = requestDto.getRepresentativeName();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.businessNumber = requestDto.getBusinessNumber();
        this.businessAddress = requestDto.getBusinessAddress();
        this.openingDate = requestDto.getOpeningDate();
        this.onlineReportNumber = requestDto.getOnlineReportNumber();
        this.accountBank = requestDto.getAccountBank();
        this.accountNumber = requestDto.getAccountNumber();
        this.isLooppass = requestDto.isLooppass();
        this.isReservationService = requestDto.isReservationService();
        this.isRefundable = requestDto.isRefundable();
        this.companyLatePlanId = companyLatePlanId;
        this.createStatus = CreateStatusEnum.WAIT.getTypeValue();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }

    public void updateCreateStatus(int status) {
        this.createStatus = status;
    }

    public void deleteCompany() {
        this.isDelete = true;
    }

    public void addRole(Role role) {
        roleSet.add(role);
    }
}
