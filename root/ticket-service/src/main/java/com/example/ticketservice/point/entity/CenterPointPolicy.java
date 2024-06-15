package com.example.ticketservice.point.entity;

import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterPointPolicy extends TimeStamped {
    @Id
    @Column(name = "center_point_policy")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long centerId;

    private int usableScope;

    private int additionalPointPercentage;


}
