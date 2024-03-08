package com.example.companyservice.entity;

import com.example.companyservice.dto.request.CompanyUpdateRequestDto;
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

    private Boolean isDutyFree;

    private String companyName;

    private String representativeName;

    private String phoneNumber;

    private String businessNumber;

    private String businessAddress;

    private LocalDate openingDate;

    private String accountBank;

    private String accountNumber;

    private Boolean isLooppass;

    private Boolean isReservationService;

    private int createStatus;

    private Boolean isOption1;

    private Boolean isOption2;

    private boolean isDelete;

    private Long companyLatePlanId;

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
        this.accountBank = requestDto.getAccountBank();
        this.accountNumber = requestDto.getAccountNumber();
        this.isLooppass = requestDto.isLooppass();
        this.isReservationService = requestDto.isReservationService();
        this.companyLatePlanId = companyLatePlanId;
        this.createStatus = CreateStatusEnum.WAIT.getTypeValue();
    }

    public void updateCreateStatus(int status) {
        this.createStatus = status;
    }

    public void deleteCompany() {
        this.isDelete = true;
    }
}
