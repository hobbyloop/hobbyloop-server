package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Payment extends TimeStamped {
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Checkout checkout;

    private String idempotencyKey;

    private Long centerId; //sellerId

    private Long ticketId;

    private Long looppassId;

    private Long amount;

    private int status; // not_started, executing, success

    private boolean isLedgerUpdated;

    private boolean isWalletUpdated;

    private int failedCount;

    private int threshold;
}
