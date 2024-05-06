package com.example.ticketservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTicketApprovedEvent {
    private Long centerId;
    private Long memberId;
}
