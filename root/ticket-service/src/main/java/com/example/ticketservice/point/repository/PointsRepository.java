package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointsRepository extends JpaRepository<Points, Long> {
    Optional<Points> findByMemberIdAndUsableScopeIs(Long memberId, int usableScope); // General 포인트 잔액 조회

    Optional<Points> findByMemberIdAndCompanyId(Long memberId, Long companyId);

    Optional<Points> findByMemberIdAndCenterId(Long memberId, Long centerId);

    List<Points> findByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
