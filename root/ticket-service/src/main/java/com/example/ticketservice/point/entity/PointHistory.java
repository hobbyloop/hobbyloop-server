package com.example.ticketservice.point.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PointHistory extends TimeStamped {
    @Id
    @Column(name = "point_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long companyId;

    private Long centerId;

    // PointTypeEnum
    private int type;

    private Long amount;

    // 기록된 당시의 잔액
    private Long balance;

    private String description;

    public boolean isGeneralPoint() {
        return companyId == null && centerId == null;
    }

    public boolean isCompanyPoint() {
        return companyId != null && centerId == null;
    }

    public boolean isCenterPoint() {
        return companyId == null && centerId != null;
    }

    public static PointHistory use(Long memberId, Long amount, Long balance, String description) {
        return PointHistory.builder()
                .memberId(memberId)
                .type(PointTypeEnum.USE.getValue())
                .amount(amount)
                .balance(balance)
                .description(description + " 구매")
                .build();
    }
}
