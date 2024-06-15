package com.example.ticketservice.point.repository;

import com.example.ticketservice.point.entity.CompanyPointPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyPointPolicyRepository extends JpaRepository<CompanyPointPolicy, Long> {
    Optional<CompanyPointPolicy> findByCompanyId(Long companyId);
}
