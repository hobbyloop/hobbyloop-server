package com.example.ticketservice.pay.repository.payment;

import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.ticket.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {

    Optional<Payment> findByIdAndIdempotencyKey(Long id, String idempotencyKey);

    List<Payment> findByCenterId(Long centerId);

    Optional<Payment> findByPspPaymentKey(String pspPaymentKey);

    Optional<Payment> findByUserTicket(UserTicket userTicket);

    @Modifying(clearAutomatically = true)
    @Query("update Payment p set p.userTicket = :userTicket where p.id = :id")
    int updateUserTicket(@Param("userTicket") UserTicket userTicket, @Param("id") Long id);
}
