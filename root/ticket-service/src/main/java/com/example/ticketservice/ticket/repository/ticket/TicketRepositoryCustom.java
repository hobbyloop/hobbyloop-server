package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryCustom {

    List<Ticket> getTicketList(long centerId, long ticketId);

    Optional<Ticket> getMinimumPriceTicket(long centerId);

    List<Ticket> getTicketListByCategory(int category, int sortId, double score, int PgeNo);

    PurchaseHistoryInOneWeekResponseDto getTicketHighestIssueCount(long centerId);
}
