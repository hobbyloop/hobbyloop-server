package com.example.ticketservice.ticket.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class LectureWaiting extends TimeStamped {

    @Id
    @Column(name = "lecture_waiting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_reservation_id")
    private LectureReservation lectureReservation;
}
