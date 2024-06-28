package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByMemberIdAndUsableScopeIs(Long memberId, int usableScope); // General 포인트 잔액 조회

    Optional<Point> findByMemberIdAndCompanyId(Long memberId, Long companyId);

    Optional<Point> findByMemberIdAndCenterId(Long memberId, Long centerId);

    List<Point> findByMemberId(Long memberId);
}
