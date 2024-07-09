package com.example.ticketservice.pay.entity.member.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class PointUsage {
    private Long pointsId;
    private Long usedAmount;
    private List<Long> pointIds = new ArrayList<>();

    public PointUsage(Long pointsId, Long usedAmount) {
        this.pointsId = pointsId;
        this.usedAmount = usedAmount;
    }

    public void addPoint(Long pointId) {
        pointIds.add(pointId);
    }
}
