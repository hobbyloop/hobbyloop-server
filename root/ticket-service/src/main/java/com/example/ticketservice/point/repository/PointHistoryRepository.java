package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {
    List<PointHistory> findByMemberId(Long memberId);
}