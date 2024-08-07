package com.example.ticketservice.pay.repository.transactionerror;

import com.example.ticketservice.pay.entity.member.TransactionError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionErrorRepository extends JpaRepository<TransactionError, Long> {
    List<TransactionError> findAllByStatusAndCreatedAtBetweenOrderByCreatedAtAsc(int status, LocalDateTime startDate, LocalDateTime endDate);

    void deleteAllByTransactionAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByPspTransactionKeyAndTransactionAtAndPspAndType(String pspTransactionKey, LocalDateTime transactionAt, int psp, int type);
}
