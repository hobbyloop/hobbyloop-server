package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryCustom {

    List<Ticket> getTicketList(long centerId, long ticketId);

    Optional<Ticket> getMinimumPriceTicket(long centerId);

    List<Ticket> getTicketListByCategoryAroundMe(int category, int sortId, int refundable, double score);

    List<Ticket> getTicketListByCategory(int category, int sortId, int refundable, double score, int PgeNo, List<String> locations);

    PurchaseHistoryInOneWeekResponseDto getTicketHighestIssueCount(long centerId);
}
