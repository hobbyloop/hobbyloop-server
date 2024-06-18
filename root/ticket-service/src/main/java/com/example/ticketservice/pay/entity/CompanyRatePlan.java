package com.example.ticketservice.pay.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.dto.request.CompanyRatePlanRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CompanyRatePlan extends TimeStamped {

    @Id
    @Column(name = "company_rate_plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int entryPermission;

    private int paymentType;

    private int price;

    private int vat;

    private int totalPrice;

    public static CompanyRatePlan from(CompanyRatePlanRequestDto requestDto) {
        return CompanyRatePlan.builder()
                .entryPermission(EntryAuthorityEnum.findByName(requestDto.getEntryPermission()).getEntryAuthorityType())
                .paymentType(CompanyPaymentType.findByName(requestDto.getPaymentType()).getPaymentType())
                .price(requestDto.getPrice())
                .vat(requestDto.getVat())
                .totalPrice(requestDto.getTotalPrice())
                .build();
    }
}
