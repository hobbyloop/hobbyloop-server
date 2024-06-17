package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.dto.response.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface UserTicketService {
    Long purchaseTicket(long memberId, long ticketId);

    void processOfflinePurchaseTicket(long memberId, long ticketId);

    List<UnapprovedUserTicketListResponseDto> getUnapprovedUserTicketList(long centerId);

    void approveUserTicket(long userTicketId);

    List<AvailableUserTicketsWithCenterInfo> getAvailableUserTicketList(long memberId);

    Long getAvailableUserTicketCount(Long memberId);

    Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>> getRecentPurchaseUserTicketList(long memberId);

    List<UserTicketUsingHistoryResponseDto> getUserTicketUsingHistory(long memberId);

    List<UserTicketExpiringHistoryResponseDto> getUserTicketExpiringHistory(long memberId);

}
