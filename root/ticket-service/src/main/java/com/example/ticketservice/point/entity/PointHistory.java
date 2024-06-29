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

    // type이 USE, EXPIRE일 시, NULL
    private LocalDateTime expirationDateTime;

    // type이 EARN이 아닐 시, NULL
    private Boolean isProcessedByBatch;

    // type이 EARN이 아닐 시, NULL
    private Boolean isExpiringSoon;

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

    public void processByBatch() {
        isProcessedByBatch = true;
    }

    public void markExpiredSoon() {
        isExpiringSoon = true;
    }

    public static PointHistory use(Point point, Long amount) {
        return PointHistory.builder()
                .memberId(point.getMemberId())
                .companyId(point.getCompanyId())
                .centerId(point.getCenterId())
                .type(PointTypeEnum.USE.getValue())
                .amount(amount)
                .balance(point.getBalance())
                .description("구매에 포인트 사용")
                .build();
    }
}
