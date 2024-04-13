package com.example.payservice.entity;

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

    private String ticketName;

    private String category;

    private String centerLogo;

    private Long memberId;

    private Long ticketId;

    private Long memberCouponId;

    private Long looppassId;
}
