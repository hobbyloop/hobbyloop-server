package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Ticket extends TimeStamped {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int category;

    private String name;

    private String introduce;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    private int duration;

    private int useCount;

    private int isTotalCount;

    private int totalCount;

    private int price;

    private int vat;

    private int discountRate;

    private int calculatedPrice;

    private int refundRegulation;

    private int refundPercentage;

    private int periodAfterPurchase;

    private int issueCount;

    private int purchaseCount;

    private int score;

    private boolean isAvailable;

    private boolean isUpload;

    private Long centerId;
}
