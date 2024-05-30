package com.example.ticketservice.ticket.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CenterMemberJoinedEvent {
    private Long memberId;
    private Long ticketId;
}
