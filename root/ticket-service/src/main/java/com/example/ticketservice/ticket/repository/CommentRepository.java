package com.example.ticketservice.ticket.repository;

import com.example.ticketservice.ticket.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByReviewId(long reviewId);

    List<Comment> findAllByReviewIdOrderByCreatedAt(long reviewId);
}
