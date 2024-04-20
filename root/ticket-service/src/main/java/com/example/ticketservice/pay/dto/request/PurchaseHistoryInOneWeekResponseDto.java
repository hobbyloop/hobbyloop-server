package com.example.ticketservice.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistoryInOneWeekResponseDto {

    private Long id;

    private String name;

    private int category;

    private int calculatedPrice;

    private int count;
}
