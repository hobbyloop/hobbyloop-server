package com.example.ticketservice.ticket.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTicketApprovedEvent {
    private Long centerId;
    private Long memberId;
}
