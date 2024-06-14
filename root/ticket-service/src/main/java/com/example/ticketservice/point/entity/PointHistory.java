package com.example.ticketservice.point.entity;

import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PointHistory extends TimeStamped {
    @Id
    @Column(name = "point_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long companyId;

    // PointTypeEnum
    private int type;

    private Long amount;

    // 기록된 당시의 잔액
    private Long balance;

    // type이 EXPIRE일 시, NULL
    private LocalDateTime expirationDateTime;

    private String description;
}
