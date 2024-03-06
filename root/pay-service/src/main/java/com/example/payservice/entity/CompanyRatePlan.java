package com.example.payservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CompanyRatePlan extends TimeStamped {

    @Id
    @Column(name = "company_rate_plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int entryPermission;

    private int paymentType;

    private int price;

    private int vat;

    private int totalPrice;

    private boolean isDelete;
}
