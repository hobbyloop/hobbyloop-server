package com.example.ticketservice.pay.entity.member;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    private int type;

    private LocalDateTime date;

    private int amount;

    private int usePoint;

    private boolean isRefund;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Long memberCouponId;

    private Long looppassId;

    private int previousStatus;

    private int newStatus;

    private String updateReason;

    public static PurchaseHistory of(Long memberId,
                                     Ticket ticket,
                                     Long memberCouponId) {
        return PurchaseHistory.builder()
                .type(1)
                .date(LocalDateTime.now())
                .amount(10000)
                .usePoint(1000)
                .isRefund(false)
                .memberId(memberId)
                .ticket(ticket)
                .memberCouponId(memberCouponId)
                .build();
    }

    public static PurchaseHistory record() {

    }
}
