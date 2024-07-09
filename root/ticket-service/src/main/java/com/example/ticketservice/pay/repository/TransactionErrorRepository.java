package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.TransactionError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionErrorRepository extends JpaRepository<TransactionError, Long> {
    List<TransactionError> findAllByStatusAndCreatedAtBetweenOrderByCreatedAtAsc(int status, LocalDateTime startDate, LocalDateTime endDate);
}
