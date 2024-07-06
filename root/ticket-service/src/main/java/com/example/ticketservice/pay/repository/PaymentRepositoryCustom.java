package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepositoryCustom {
    List<Payment> findPendingPayments(LocalDateTime updatedAt);
}
