package com.example.ticketservice.coupon.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class CenterInfo {
    private Long id;
    private String name;
}
