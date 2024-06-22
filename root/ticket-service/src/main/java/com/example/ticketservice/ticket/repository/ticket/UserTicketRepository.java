package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.UserTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long>, UserTicketRepositoryCustom {
    List<UserTicket> findAllByTicketId(Long ticketId);

    List<UserTicket> findAllByTicketIdAndIsApproveFalse(long ticketId);

    List<UserTicket> findAllByMemberId(long memberId);

    List<UserTicket> findAllByMemberIdOrderByCreatedAtDesc(long memberId);

    Optional<UserTicket> findFirstByMemberIdOrderByCreatedAtDesc(long memberId);

    Page<UserTicket> findByEndDateBefore(LocalDate endDate, Pageable pageable);
}
