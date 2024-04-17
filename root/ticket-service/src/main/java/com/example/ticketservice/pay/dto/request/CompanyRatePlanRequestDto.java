package com.example.ticketservice.pay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyRatePlanRequestDto {

    private String entryPermission;

    private String paymentType;

    private int price;

    private int vat;

    private int totalPrice;
}
