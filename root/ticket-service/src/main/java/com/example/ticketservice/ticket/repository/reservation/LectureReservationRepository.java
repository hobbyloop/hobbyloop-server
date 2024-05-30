package com.example.ticketservice.ticket.repository.reservation;

import com.example.ticketservice.ticket.entity.LectureReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureReservationRepository extends JpaRepository<LectureReservation, Long> {
}
