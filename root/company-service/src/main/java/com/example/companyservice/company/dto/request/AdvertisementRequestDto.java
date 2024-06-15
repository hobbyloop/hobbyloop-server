package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String campaignName;

    @NotBlank
    private String adType;

    private int postingCount;

    private int clickCount;

    @Max(value = 25)
    @Min(value = 0)
    private int adRank;

    private int price;

    private String content;

    private String keyword;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate adStart;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate adEnd;

    @NotNull
    private int adPrice;

    @NotNull
    private int discountPrice;

    @NotNull
    private int vat;

    @NotNull
    private int totalPrice;

    @NotBlank
    private String paymentType;

    @NotBlank
    private String bankName;

    @NotBlank
    private String accountNumber;
}
