package com.example.ticketservice.ticket.event;

import com.example.ticketservice.ticket.service.UserTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CenterMembershipEventHandler {
    private final UserTicketService userTicketService;

    @TransactionalEventListener
    public void handleCenterMemberJoinedEvent(CenterMemberJoinedEvent event) {
        userTicketService.processOfflinePurchaseTicket(event.getMemberId(), event.getTicketId());
    }
}
