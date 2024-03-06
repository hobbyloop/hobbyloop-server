package com.example.companyservice.entity;

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

    private boolean isDutyFree;

    private String companyName;

    private String representativeName;

    private String phoneNumber;

    private String businessNumber;

    private String businessAddress;

    private LocalDate openingDate;

    private String accountBank;

    private String accountNumber;

    private boolean isFreePass;

    private String createStatus;

    private boolean isOption1;

    private boolean isOption2;

    private boolean isDelete;

    private Long companyLatePlanId;
}
