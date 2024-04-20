package com.example.ticketservice.repository;

import com.example.ticketservice.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    boolean existsByReviewIdAndMemberId(long reviewId, long memberId);
}
