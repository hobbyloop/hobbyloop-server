package com.example.ticketservice.coupon.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class CompanyInfo {
    private Long id;
    private String name;
}
