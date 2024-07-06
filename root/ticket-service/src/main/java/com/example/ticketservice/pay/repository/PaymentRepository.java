package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {

    Optional<Payment> findByIdAndIdempotencyKey(Long id, String idempotencyKey);

    List<Payment> findByCenterId(Long centerId);
}
