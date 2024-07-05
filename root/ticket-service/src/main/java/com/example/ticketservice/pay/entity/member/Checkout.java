package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.entity.member.vo.PointUsage;
import com.example.ticketservice.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Checkout extends TimeStamped {
    @Id
    @Column(name = "checkout_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId; // buyerId

    private Long ticketId;

    private boolean isPaymentDone;

    private String idempotencyKey;

    private String pspPaymentKey;

    private int type; // 결제 유형?

    private int method; // card, ...

    private Long memberCouponId;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "used_points", joinColumns = @JoinColumn(name = "checkout_id"))
    private List<PointUsage> pointUsages;

    private Long originalAmount;

    private Long totalDiscountAmount;

    private Long finalAmount;

    private Long couponDiscountAmount;

    private Long pointDiscountAmount;

    private LocalDateTime approvedAt;

    public static Checkout prepare(Long memberId, Ticket ticket) {
        return Checkout.builder()
                .memberId(memberId)
                .ticketId(ticket.getId())
                .isPaymentDone(false)
                .idempotencyKey(UUID.randomUUID().toString())
                .originalAmount(Long.valueOf(ticket.getCalculatedPrice()))
                .build();
    }

    public void checkout(CheckoutRequestDto response, List<PointUsage> pointUsages) {
        this.type = response.getType();
        this.method = response.getMethod();
        this.memberCouponId = response.getMemberCouponId();
        this.pointUsages = pointUsages;
        this.totalDiscountAmount = response.getTotalDiscountAmount();
        this.finalAmount = response.getFinalAmount();
        this.couponDiscountAmount = response.getCouponDiscountAmount();
        this.pointDiscountAmount = response.getPoints();
    }

    public void done() {
        this.isPaymentDone = true;
    }
}
