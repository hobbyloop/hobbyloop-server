package com.example.ticketservice.pay.entity;

import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PurchaseHistory extends TimeStamped {

    @Id
    @Column(name = "purchase_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int type;

    private LocalDateTime date;

    private int price;

    private int usePoint;

    private boolean isRefund;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Long memberCouponId;

    private Long looppassId;

    public static PurchaseHistory of(Long memberId,
                                     Ticket ticket,
                                     Long memberCouponId) {
        return PurchaseHistory.builder()
                .type(1)
                .date(LocalDateTime.now())
                .price(10000)
                .usePoint(1000)
                .isRefund(false)
                .memberId(memberId)
                .ticket(ticket)
                .memberCouponId(memberCouponId)
                .build();
    }
}
