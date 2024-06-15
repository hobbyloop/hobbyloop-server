package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.PointEventPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointEventPolicyRepository extends JpaRepository<PointEventPolicy, Long> {

    @Query("SELECT p FROM PointEventPolicy p WHERE :now BETWEEN p.startDateTime AND p.endDateTime")
    List<PointEventPolicy> findActiveEvents(@Param("now") LocalDateTime now);
}
