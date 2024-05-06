package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.UserTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long>, UserTicketRepositoryCustom {

    List<UserTicket> findAllByTicketIdAndIsApproveFalse(long ticketId);

    List<UserTicket> findAllByMemberId(long memberId);

    Optional<UserTicket> findFirstByMemberIdOrderByCreatedAtDesc(long memberId);

    Page<UserTicket> findByEndDateBefore(LocalDate endDate, Pageable pageable);
}
