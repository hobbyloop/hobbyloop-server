package com.example.ticketservice.ticket.entity;

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

    private Long lectureScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ticket_id")
    private UserTicket userTicket;

    private Long lecturePassId;

    private Long memberId;

    private int remainingCount;

    public static LectureReservation of(UserTicket userTicket, Long lectureScheduleId, Long memberId) {
        return LectureReservation.builder()
                .status(ReservationStatusEnum.CONFIRM.getReservationStatusType())
                .userTicket(userTicket)
                .lectureScheduleId(lectureScheduleId)
                .remainingCount(userTicket.getRemainingCount())
                .memberId(memberId)
                .build();
    }
}
