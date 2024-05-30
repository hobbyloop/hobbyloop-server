package com.example.ticketservice.ticket.event;

import com.example.ticketservice.ticket.service.CenterMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserTicketEventHandler {
    private final CenterMembershipService centerMembershipService;

    @TransactionalEventListener
    public void handleUserTicketApprovedEvent(UserTicketApprovedEvent event) {
        centerMembershipService.joinCenterMembership(event.getCenterId(), event.getMemberId());
    }
}
