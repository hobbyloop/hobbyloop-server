package com.example.ticketservice.service;

import com.example.ticketservice.entity.LectureReservation;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.repository.reservation.LectureReservationRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureReservationServiceImpl implements LectureReservationService {
    private final LectureReservationRepository lectureReservationRepository;

    private final UserTicketRepository userTicketRepository;

    @Override
    @Transactional
    public Long reserveLecture(long memberId, long userTicketId, long lectureScheduleId) {
        UserTicket userTicket = userTicketRepository.findById(userTicketId)
                .orElseThrow(() -> new IllegalArgumentException("UserTicket not found"));

        // TODO: LectureSchedule 정원 수 체크 (LectureClient로 확인)
        // TODO: LectureSchedule 예약 수 증가 (LectureClient로 요청)

        // 성공 시 예약 정보 저장
        LectureReservation lectureReservation = LectureReservation.of(userTicket, lectureScheduleId, memberId);
        lectureReservationRepository.save(lectureReservation);

        userTicket.use();

        return lectureReservation.getId();
    }
}
