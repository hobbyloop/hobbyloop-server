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

    private String name;

    private String introduce;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    private int periodAfterPurchase;

    private int hasTotalCount;

    private int totalCount;

    private int issueCount;

    private int price;

    private int vat;

    private int discountRate;

    private int calculatedPrice;

    private boolean isAvailable;

    private int category;

    private int refundRegulation;

    private int refundPercentage;

    private int duration;

    private int useCount;

    private int purchaseCount;

    private boolean isUpload;

    private Long centerId;
}
