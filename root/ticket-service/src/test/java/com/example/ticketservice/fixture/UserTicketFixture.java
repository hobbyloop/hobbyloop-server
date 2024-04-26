package com.example.ticketservice.fixture;

import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.entity.UserTicket;

import java.time.LocalDate;

public class UserTicketFixture {

    public static UserTicket expiredByEndDateUserTicket(Ticket ticket, Long memberId) {
        LocalDate now = LocalDate.now();
        return UserTicket.builder()
                .ticket(ticket)
                .startDate(now.minusDays(10))
                .endDate(now.minusDays(1))
                .remainingCount(2)
                .memberId(memberId)
                .build();
    }

    public static UserTicket expiredByRemainingCountUserTicket(Ticket ticket, Long memberId) {
        LocalDate now = LocalDate.now();
        return UserTicket.builder()
                .ticket(ticket)
                .startDate(now.minusDays(10))
                .endDate(now.plusDays(1))
                .remainingCount(0)
                .memberId(memberId)
                .build();
    }

    public static UserTicket activeUserTicket(Ticket ticket, Long memberId) {
        LocalDate now = LocalDate.now();
        return UserTicket.builder()
                .ticket(ticket)
                .startDate(now.minusDays(10))
                .endDate(now.plusDays(1))
                .remainingCount(2)
                .memberId(memberId)
                .build();
    }

}
