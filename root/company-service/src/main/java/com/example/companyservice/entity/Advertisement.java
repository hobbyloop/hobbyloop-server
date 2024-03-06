package com.example.companyservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Advertisement extends TimeStamped {

    @Id
    @Column(name = "advertisement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String campaignName;

    private int adType;

    private int price;

    private String bannerImage;

    private String content;

    private String keyword;

    private LocalDate adStart;

    private LocalDate adEnd;

    private int adRanking;

    private int adPrice;

    private int discountPrice;

    private int vat;

    private int paymentType;

    private int totalPrice;

    private String bankName;

    private String accountNumber;

    private boolean isOpen;

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;
}
