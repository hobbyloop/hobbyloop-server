package com.example.ticketservice.ticket.repository.reservation;

import com.example.ticketservice.ticket.entity.LectureReservation;
import com.example.ticketservice.ticket.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureReservationRepository extends JpaRepository<LectureReservation, Long> {
    List<LectureReservation> findByUserTicket(UserTicket userTicket);
}
