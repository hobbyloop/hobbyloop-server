package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    List<Ticket> findAllByCenterId(long centerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.id = :ticketId")
    Optional<Ticket> findForUpdate(long ticketId);
}
