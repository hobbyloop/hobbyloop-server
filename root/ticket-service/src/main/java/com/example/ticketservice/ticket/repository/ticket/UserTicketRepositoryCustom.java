package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.UserTicket;

import java.util.List;

public interface UserTicketRepositoryCustom {

    List<UserTicket> findAvailableUserTicketList(long memberId);

    Long countAvailableUserTicketList(long memberId);
}
