package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserTicket extends TimeStamped {

    @Id
    @Column(name = "user_ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private int remainingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Long memberId;

    private boolean isApprove;

    private LocalDateTime approveTime;

    private boolean isDelete;

    public static UserTicket of(Ticket ticket, Long memberId) {
        return UserTicket.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(ticket.getDuration()))
                .remainingCount(ticket.getUseCount())
                .ticket(ticket)
                .memberId(memberId)
                .build();
    }

    public void approve() {
        this.isApprove = true;
        this.approveTime = LocalDateTime.now();
    }

    public void use() {
        if (this.remainingCount <= 0) {
            throw new IllegalArgumentException("No remaining ticket");
        }
        if (this.endDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expired ticket");
        }

        this.remainingCount--;
    }
}
