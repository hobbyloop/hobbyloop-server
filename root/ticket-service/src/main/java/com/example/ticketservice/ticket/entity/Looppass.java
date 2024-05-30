package com.example.ticketservice.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Looppass extends TimeStamped {

    @Id
    @Column(name = "looppass_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int availablePrice;

    private LocalDate startTime;

    private LocalDate endTime;

    private int price;
}
