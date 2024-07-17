package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PaymentRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRefundRepository extends JpaRepository<PaymentRefund, Long> {
    Optional<PaymentRefund> findByPaymentAndPspPaymentKey(Payment payment, String pspPaymentKey);

    Optional<PaymentRefund> findByPspPaymentKeyAndIsReconciledFalse(String pspPaymentKey);
}
