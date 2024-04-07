package com.example.companyservice.entity;

import com.example.companyservice.dto.request.AdvertisementRequestDto;
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

    private int adRank;

    private int adPrice;

    private int discountPrice;

    private int vat;

    private int paymentType;

    private int totalPrice;

    private String bankName;

    private String accountNumber;

    private boolean isOpen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static Advertisement of(AdvertisementRequestDto requestDto, Center center) {
        return Advertisement.builder()
                .campaignName(requestDto.getCampaignName())
                .adType(AdvertisementTypeEnum.findByName(requestDto.getAdType()).getTypeValue())
                .price(requestDto.getPrice())
                .content(requestDto.getContent())
                .keyword(requestDto.getKeyword())
                .adStart(requestDto.getAdStart())
                .adEnd(requestDto.getAdEnd())
                .adRank(requestDto.getAdRank())
                .adPrice(requestDto.getAdPrice())
                .discountPrice(requestDto.getDiscountPrice())
                .vat(requestDto.getVat())
                .paymentType(PaymentTypeEnum.findByName(requestDto.getPaymentType()).getTypeValue())
                .totalPrice(requestDto.getTotalPrice())
                .bankName(requestDto.getBankName())
                .accountNumber(requestDto.getAccountNumber())
                .isOpen(false)
                .center(center)
                .build();
    }

    public void updateIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
