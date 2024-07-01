package com.example.ticketservice.point.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Point extends TimeStamped {
    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Points points;

    private Long memberId;

    private Long companyId;

    private Long centerId;

    private Long amount;

    private int usableScope;

    private boolean isExpiringSoon;

    private LocalDateTime expirationDateTime;

    public void subtract(Long amount) {
        this.amount -= amount;
    }

    public void markExpiredSoon() {
        this.isExpiringSoon = true;
    }
}
