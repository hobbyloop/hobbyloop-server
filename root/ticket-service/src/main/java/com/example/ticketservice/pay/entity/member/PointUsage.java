package com.example.ticketservice.pay.entity.member;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class PointUsage {
    private Long pointId;
    private Long usedAmount;
}
