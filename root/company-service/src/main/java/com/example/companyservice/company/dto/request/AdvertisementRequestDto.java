package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementRequestDto {

    private String campaignName;

    private String adType;

    private int postingCount;

    private int clickCount;

    private int adRank;

    private int price;

    private String content;

    private String keyword;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate adStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate adEnd;

    private int adPrice;

    private int discountPrice;

    private int vat;

    private int totalPrice;

    private String paymentType;

    private String bankName;

    private String accountNumber;
}
