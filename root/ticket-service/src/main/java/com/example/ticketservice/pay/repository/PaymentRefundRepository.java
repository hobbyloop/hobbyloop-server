package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.PaymentRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRefundRepository extends JpaRepository<PaymentRefund, Long> {
}
