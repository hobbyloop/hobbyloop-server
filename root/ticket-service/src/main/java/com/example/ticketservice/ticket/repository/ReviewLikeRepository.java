package com.example.ticketservice.ticket.repository;

import com.example.ticketservice.ticket.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    boolean existsByReviewIdAndMemberId(long reviewId, long memberId);
}
