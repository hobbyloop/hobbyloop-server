package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.CenterPointPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CenterPointPolicyRepository extends JpaRepository<CenterPointPolicy, Long> {
    Optional<CenterPointPolicy> findByCenterId(Long centerId);
}
