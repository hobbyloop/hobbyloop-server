package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.UserTicket;

import java.util.List;

public interface UserTicketRepositoryCustom {

    List<UserTicket> findAvailableUserTicketList(long memberId);
}
