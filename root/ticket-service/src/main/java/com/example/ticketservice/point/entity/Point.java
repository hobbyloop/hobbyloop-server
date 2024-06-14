package com.example.ticketservice.point.entity;

import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Point extends TimeStamped {
    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long companyId;

    private Long balance;

    private int usableScope;


    public void earn(Long amount) {
        balance += amount;
    }

    public void use(Long amount) {
        balance -= amount;
    }
}
