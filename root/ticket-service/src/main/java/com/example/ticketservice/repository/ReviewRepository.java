package com.example.ticketservice.repository;

import com.example.ticketservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByTicketId(long ticketId);

    int countByCenterId(long centerId);
}
