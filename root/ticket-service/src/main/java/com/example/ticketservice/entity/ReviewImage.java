package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ReviewImage {

    @Id
    @Column(name = "review_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewImageKey;

    private String reviewImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public static ReviewImage of(String reviewImageKey, String reviewImageUrl, Review review, Ticket ticket) {
        return ReviewImage.builder()
                .reviewImageKey(reviewImageKey)
                .reviewImageUrl(reviewImageUrl)
                .review(review)
                .ticket(ticket)
                .build();
    }
}