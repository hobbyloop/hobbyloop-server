package com.example.ticketservice.pay.entity.member.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointUsage {
    private Long pointId;
    private Long usedAmount;
}
