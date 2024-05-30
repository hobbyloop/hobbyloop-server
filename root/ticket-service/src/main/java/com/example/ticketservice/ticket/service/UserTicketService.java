package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.dto.response.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.ticket.dto.response.RecentPurchaseUserTicketListResponseDto;
import com.example.ticketservice.ticket.dto.response.UnapprovedUserTicketListResponseDto;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface UserTicketService {
    Long purchaseTicket(long memberId, long ticketId);

    void processOfflinePurchaseTicket(long memberId, long ticketId);

    List<UnapprovedUserTicketListResponseDto> getUnapprovedUserTicketList(long centerId);

    void approveUserTicket(long userTicketId);

    Map<String, AvailableUserTicketsWithCenterInfo> getAvailableUserTicketList(long memberId);

    Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>> getRecentPurchaseUserTicketList(long memberId);
}
