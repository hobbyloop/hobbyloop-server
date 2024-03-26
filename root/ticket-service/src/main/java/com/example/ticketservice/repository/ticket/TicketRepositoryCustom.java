package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.Ticket;

import java.util.List;

public interface TicketRepositoryCustom {

    List<Ticket> getTicketList(long centerId, long ticketId);
}
