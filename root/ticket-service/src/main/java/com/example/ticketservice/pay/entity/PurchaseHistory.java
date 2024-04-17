package com.example.ticketservice.pay.entity;

import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.entity.TimeStamped;
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

    private String centerName;

    private String centerLogo;

    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Long memberCouponId;

    private Long looppassId;
}
