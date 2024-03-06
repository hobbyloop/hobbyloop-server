package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class LectureWaitingList extends TimeStamped {

    @Id
    @Column(name = "lecture_waiting_list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_reservation_id")
    private LectureReservation lectureReservation;
}
