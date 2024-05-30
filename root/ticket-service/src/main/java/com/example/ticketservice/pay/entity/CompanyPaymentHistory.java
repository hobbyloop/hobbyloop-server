package com.example.ticketservice.pay.entity;

import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CompanyPaymentHistory extends TimeStamped {

    @Id
    @Column(name = "company_payment_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int paymentStatus;

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_rate_plan_id")
    private CompanyRatePlan companyRatePlan;

    private Long companyId;
}
