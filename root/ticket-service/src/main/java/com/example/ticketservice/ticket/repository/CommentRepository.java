package com.example.ticketservice.ticket.repository;

import com.example.ticketservice.ticket.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c.content from Comment c where c.review.id = :reviewId")
    List<String> findAllCommentByReviewId(@Param("reviewId") long reviewId);

    List<Comment> findAllByReviewIdOrderByCreatedAt(long reviewId);
}
