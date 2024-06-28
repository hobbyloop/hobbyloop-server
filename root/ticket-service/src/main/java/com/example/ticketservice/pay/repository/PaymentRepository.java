package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdAndIdempotencyKey(Long id, String idempotencyKey);
}
