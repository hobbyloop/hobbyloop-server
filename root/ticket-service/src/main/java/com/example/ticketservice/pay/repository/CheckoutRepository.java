package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Optional<Checkout> findByMemberIdAndTicketIdAndIdempotencyKey(Long memberId, Long ticketId, String idempotencyKey);
}
