package com.example.companyservice.entity;

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

    private int score;

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
}
