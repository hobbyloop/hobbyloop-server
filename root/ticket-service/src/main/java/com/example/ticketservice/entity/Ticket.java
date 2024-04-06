package com.example.ticketservice.entity;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.request.TicketUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Ticket extends TimeStamped {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int category;

    private String name;

    private String ticketImageKey;

    private String ticketImageUrl;

    private String introduce;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    private int duration;

    private int useCount;

    private boolean isTotalCount;

    private int totalCount;

    private int price;

    private int vat;

    private int discountRate;

    private int calculatedPrice;

    private int refundRegulation;

    private int refundPercentage;

    private int periodAfterPurchase; // 확인필요

    private int issueCount; // 확인필요

    private int purchaseCount; // 확인필요

    private boolean isAvailable; // 확인필요

    private boolean isUpload;

    private Long centerId;

    public static Ticket of(long centerId,
                            String ticketImageKey,
                            String ticketImageUrl,
                            TicketCreateRequestDto requestDto) {
        return Ticket.builder()
                .category(CategoryEnum.findByName(requestDto.getCategory()).getCategoryType())
                .name(requestDto.getName())
                .ticketImageKey(ticketImageKey)
                .ticketImageUrl(ticketImageUrl)
                .introduce(requestDto.getIntroduce())
                .expirationStartDate(requestDto.getExpirationStartDate())
                .expirationEndDate(requestDto.getExpirationEndDate())
                .duration(requestDto.getDuration())
                .useCount(requestDto.getUseCount())
                .isTotalCount(requestDto.isTotalCount())
                .totalCount(requestDto.getTotalCount())
                .price(requestDto.getPrice())
                .vat(requestDto.getVat())
                .discountRate(requestDto.getDiscountRate())
                .calculatedPrice(requestDto.getCalculatedPrice())
                .refundRegulation(requestDto.getRefundRegulation())
                .refundPercentage(requestDto.getRefundPercentage())
                .centerId(centerId)
                .build();
    }

    public void updateTicketImage(String ticketImageKey, String ticketImageUrl) {
        this.ticketImageKey = ticketImageKey;
        this.ticketImageUrl = ticketImageUrl;
    }

    public void update(TicketUpdateRequestDto requestDto) {
        this.name = requestDto.getName();
        this.category = CategoryEnum.findByName(requestDto.getCategory()).getCategoryType();
        this.introduce = requestDto.getIntroduce();
        this.expirationStartDate = requestDto.getExpirationStartDate();
        this.expirationEndDate = requestDto.getExpirationEndDate();
        this.duration = requestDto.getDuration();
        this.useCount = requestDto.getUseCount();
        this.isTotalCount = requestDto.isTotalCount();
        this.totalCount = requestDto.getTotalCount();
        this.price = requestDto.getPrice();
        this.vat = requestDto.getVat();
        this.discountRate = requestDto.getDiscountRate();
        this.calculatedPrice = requestDto.getCalculatedPrice();
        this.refundRegulation = requestDto.getRefundRegulation();
        this.refundPercentage = requestDto.getRefundPercentage();
    }

    public void upload() {
        this.isUpload = true;
    }

    public void cancelUpload() {
        this.isUpload = false;
    }
}
