package com.example.ticketservice.repository;

import com.example.ticketservice.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select ri.reviewImageUrl from ReviewImage ri where ri.ticket.id = :ticketId order by ri.id desc limit 4")
    List<String> findAllUrlByTicketId(long ticketId);

    @Query("select ri.reviewImageUrl from ReviewImage ri where ri.review.id = :reviewId")
    List<String> findAllUrlByReviewId(long reviewId);
}
