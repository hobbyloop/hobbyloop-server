package com.example.ticketservice.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserLooppass extends TimeStamped {

    @Id
    @Column(name = "user_looppass_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private int remainingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "looppass_id")
    private Looppass looppass;

    private Long memberId;
}
