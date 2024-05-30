package com.example.ticketservice.ticket.repository.review;

import com.example.ticketservice.ticket.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findAllByCenterId(long centerId);

    List<Review> findAllByTicketId(long ticketId);

    int countByCenterId(long centerId);
}
