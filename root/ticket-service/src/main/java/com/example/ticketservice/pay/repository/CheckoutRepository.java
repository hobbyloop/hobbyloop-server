package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
}
