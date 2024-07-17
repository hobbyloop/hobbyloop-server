package com.example.ticketservice.point.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.entity.member.Payment;
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

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    private Long memberId;

    private Long companyId;

    private Long centerId;

    private Long amount;

    private int usableScope;

    private boolean isExpiringSoon;

    private LocalDateTime expirationDateTime;

    private boolean isDeleted;

    public void subtract(Long amount) {
        this.amount -= amount;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public void markExpiredSoon() {
        this.isExpiringSoon = true;
    }
}
