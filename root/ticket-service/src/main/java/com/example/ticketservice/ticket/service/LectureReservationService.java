package com.example.ticketservice.ticket.service;

public interface LectureReservationService {

    Long reserveLecture(long memberId, long userTicketId, long lectureScheduleId);
}
