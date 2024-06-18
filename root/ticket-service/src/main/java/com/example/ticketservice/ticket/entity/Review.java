package com.example.ticketservice.ticket.entity;

import com.example.ticketservice.common.entity.TimeStamped;
import com.example.ticketservice.ticket.dto.request.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Review extends TimeStamped {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float score;

    private String content;

    private boolean isBlind;

    private boolean isDelete;

    private int commentCount;

    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Long memberId;

    private String nickname;

    private Long centerId;

    public static Review of(ReviewRequestDto requestDto,
                            Ticket ticket,
                            Long memberId,
                            String nickname,
                            Long centerId) {
        return Review.builder()
                .score(requestDto.getScore())
                .content(requestDto.getContent())
                .ticket(ticket)
                .memberId(memberId)
                .nickname(nickname)
                .centerId(centerId)
                .build();
    }
}
