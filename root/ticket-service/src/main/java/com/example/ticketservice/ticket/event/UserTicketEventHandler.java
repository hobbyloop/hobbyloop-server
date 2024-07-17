package com.example.ticketservice.ticket.event;

import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import com.example.ticketservice.ticket.service.CenterMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserTicketEventHandler {
    private final CenterMembershipService centerMembershipService;
    private final UserTicketRepository userTicketRepository;

    @TransactionalEventListener
    public void handleUserTicketApprovedEvent(UserTicketApprovedEvent event) {
        centerMembershipService.joinCenterMembership(event.getCenterId(), event.getMemberId());
    }
}
