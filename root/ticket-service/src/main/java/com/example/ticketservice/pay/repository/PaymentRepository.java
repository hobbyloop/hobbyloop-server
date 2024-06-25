package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
