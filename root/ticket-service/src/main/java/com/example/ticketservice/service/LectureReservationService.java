package com.example.ticketservice.service;

public interface LectureReservationService {

    Long reserveLecture(long memberId, long userTicketId, long lectureScheduleId);
}
