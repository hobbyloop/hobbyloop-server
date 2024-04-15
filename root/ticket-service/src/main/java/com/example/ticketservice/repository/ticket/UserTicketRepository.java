package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {

    List<UserTicket> findAllByTicketIdAndIsApproveFalse(long ticketId);

    List<UserTicket> findAllByMemberId(long memberId);
}
