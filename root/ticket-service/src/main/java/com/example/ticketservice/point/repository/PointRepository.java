package com.example.ticketservice.point.repository;

import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByPointsAndIsDeletedFalseOrderByCreatedAtAsc(Points points);

    List<Point> findByPointsAndIsDeletedFalse(Points points);

    List<Point> findByMemberIdAndIsExpiringSoonTrueOrderByCreatedAtAsc(Long memberId);

    void deleteAllByMemberId(Long memberId);

    Optional<Point> findByPayment(Payment payment);
}
