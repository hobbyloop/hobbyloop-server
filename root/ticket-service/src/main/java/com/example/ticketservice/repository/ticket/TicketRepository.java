package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    List<Ticket> findAllByCenterId(long centerId);
}
