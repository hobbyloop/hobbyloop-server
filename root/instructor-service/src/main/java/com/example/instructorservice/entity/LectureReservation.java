package com.example.instructorservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class LectureReservation extends TimeStamped {

    @Id
    @Column(name = "lecture_reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private Long lectureTicketId;

    private Long lecturePassId;

    private Long memberId;
}
