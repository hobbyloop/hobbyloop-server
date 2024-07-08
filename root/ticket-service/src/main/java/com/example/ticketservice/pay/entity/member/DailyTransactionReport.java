package com.example.ticketservice.pay.entity.member;

import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DailyTransactionReport extends TimeStamped {
    @Id
    @Column(name = "daily_transaction_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Integer totalTransactionCount;

    private Long totalSales;

    private Long netSales;

    private Long totalDiscountAmount;

    private Integer totalRefundCount;

    private Long refundAmount;

    private Integer checkoutCount;

    private Integer transactionErrorCount;

    private Integer paymentFailedCount;

    private Integer paymentAttemptCount;

    private Integer paymentSuccessCount;

    private Integer paymentSuccessRate;

    private Integer peakTransactionHour;

    private Integer newCustomerCount;

    private Integer returningCustomerCount;

    private Integer totalCustomerCount;
}
