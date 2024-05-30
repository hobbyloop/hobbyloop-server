package com.example.ticketservice.pay.entity;

import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class AdvertisementPointHistory extends TimeStamped {

    @Id
    @Column(name = "advertisement_point_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pointPaymentType;

    private int pointChargingPrice;

    private String bankName;

    private String pointAccount;

    private boolean isDelete;

    private Long centerId;
}
