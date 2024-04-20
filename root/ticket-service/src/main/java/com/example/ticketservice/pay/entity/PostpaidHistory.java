package com.example.ticketservice.pay.entity;

import com.example.ticketservice.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PostpaidHistory extends TimeStamped {

    @Id
    @Column(name = "postpaid_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int postpaidType;

    private int installmentMonth;

    private int remainInstallmentMonth;

    private int postpaidPrice;

    private String bankName;

    private String postpaidAccount;

    private boolean isDelete;

    private Long centerId;
}
