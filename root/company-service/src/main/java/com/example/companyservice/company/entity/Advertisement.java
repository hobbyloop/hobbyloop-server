package com.example.companyservice.company.entity;

import com.example.companyservice.company.dto.request.AdvertisementRequestDto;
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

    private Integer postingCount;

    private Integer clickCount;

    private Integer adRank;

    private int price;

    private String bannerImageKey;

    private String bannerImageUrl;

    private String content;

    private String keyword;

    private LocalDate adStart;

    private LocalDate adEnd;

    private int adPrice;

    private int discountPrice;

    private int vat;

    private int totalPrice;

    private int paymentType;

    private String bankName;

    private String accountNumber;

    private boolean isOpen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static Advertisement of(AdvertisementRequestDto requestDto,
                                   Center center,
                                   String bannerImageKey,
                                   String bannerImageUrl) {
        return Advertisement.builder()
                .campaignName(requestDto.getCampaignName())
                .adType(AdvertisementTypeEnum.findByName(requestDto.getAdType()).getTypeValue())
                .postingCount(requestDto.getPostingCount())
                .clickCount(requestDto.getClickCount())
                .adRank(requestDto.getAdRank())
                .price(requestDto.getPrice())
                .bannerImageKey(bannerImageKey)
                .bannerImageUrl(bannerImageUrl)
                .content(requestDto.getContent())
                .keyword(requestDto.getKeyword())
                .adStart(requestDto.getAdStart())
                .adEnd(requestDto.getAdEnd())
                .adPrice(requestDto.getAdPrice())
                .discountPrice(requestDto.getDiscountPrice())
                .vat(requestDto.getVat())
                .totalPrice(requestDto.getTotalPrice())
                .paymentType(PaymentTypeEnum.findByName(requestDto.getPaymentType()).getTypeValue())
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
