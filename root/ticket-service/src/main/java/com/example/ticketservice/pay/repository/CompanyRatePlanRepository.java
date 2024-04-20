package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.CompanyRatePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRatePlanRepository extends JpaRepository<CompanyRatePlan, Long> {
}
