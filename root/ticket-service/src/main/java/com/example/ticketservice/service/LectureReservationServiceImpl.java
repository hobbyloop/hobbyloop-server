package com.example.ticketservice.service;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
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
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_TICKET_NOT_EXIST_EXCEPTION));

        // TODO: 해당 이용권으로 예약 가능한지 체크

        // TODO: LectureSchedule 정원 수 체크 (LectureClient로 확인)
        // TODO: LectureSchedule 예약 수 증가 (LectureClient로 요청)

        // 성공 시 예약 정보 저장, 사용자 이용권 잔여 횟수 차감
        userTicket.use();

        LectureReservation lectureReservation = LectureReservation.of(userTicket, lectureScheduleId, memberId);
        lectureReservationRepository.save(lectureReservation);

        return lectureReservation.getId();
    }
}
