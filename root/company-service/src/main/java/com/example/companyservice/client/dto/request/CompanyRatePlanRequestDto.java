package com.example.companyservice.client.dto.request;

import com.example.companyservice.dto.request.CompanyUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRatePlanRequestDto {

    private String entryPermission;

    private String paymentType;

    private int price;

    private int vat;

    private int totalPrice;

    public static CompanyRatePlanRequestDto from(CompanyUpdateRequestDto requestDto) {
        return CompanyRatePlanRequestDto.builder()
                .entryPermission(requestDto.getEntryPermission())
                .paymentType(requestDto.getPaymentType())
                .price(requestDto.getPrice())
                .vat(requestDto.getVat())
                .totalPrice(requestDto.getTotalPrice())
                .build();
    }
}
