package com.example.ticketservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CenterMemberJoinedEvent {
    private Long memberId;
    private Long ticketId;
}
