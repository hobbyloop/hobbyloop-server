package com.example.ticketservice.repository.ticket;

import com.example.ticketservice.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryCustom {

    List<Ticket> getTicketList(long centerId, long ticketId);

    Optional<Ticket> getMinimumPriceTicket(long centerId);
}
