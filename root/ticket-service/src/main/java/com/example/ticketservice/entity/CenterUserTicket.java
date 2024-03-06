package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterUserTicket extends TimeStamped {

    @Id
    @Column(name = "center_user_ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isApprove;

    private LocalDateTime approveTime;

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ticket_id")
    private UserTicket userTicket;

    private Long centerId;
}
