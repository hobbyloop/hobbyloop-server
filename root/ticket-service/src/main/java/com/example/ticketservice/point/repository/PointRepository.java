package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByPointsOrderByCreatedAtAsc(Points points);

    List<Point> findByMemberIdAndIsExpiringSoonTrueOrderByCreatedAtAsc(Long memberId);
}
