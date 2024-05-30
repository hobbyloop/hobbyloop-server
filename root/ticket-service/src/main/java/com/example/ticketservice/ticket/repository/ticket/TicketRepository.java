package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    List<Ticket> findAllByCenterId(long centerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.id = :ticketId")
    Optional<Ticket> findForUpdate(@Param("ticketId") long ticketId);

    List<Ticket> findAllByCenterIdAndIsUploadTrueOrderByCalculatedPriceAsc(long centerId);

    List<Ticket> findAllByCenterIdAndIsUploadTrueOrderByCreatedAtDesc(long centerId);

    boolean existsByCenterId(long centerId);

    List<Ticket> findAllByCenterIdOrderByCalculatedPrice(long centerId);
}
