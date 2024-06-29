package com.example.ticketservice.coupon.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MemberCoupon extends TimeStamped {
    @Id
    @Column(name = "member_coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    private Long memberId;

    private boolean isUsed;

    private LocalDateTime usedAt;

    private LocalDateTime expirationDateTime;

    public static MemberCoupon issue(Long memberId, Coupon coupon) {
        return MemberCoupon.builder()
                .coupon(coupon)
                .memberId(memberId)
                .isUsed(false)
                .expirationDateTime(LocalDateTime.now().plusDays(coupon.getExpirationPeriodDays()))
                .build();
    }

    public void use() {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
    }

}
