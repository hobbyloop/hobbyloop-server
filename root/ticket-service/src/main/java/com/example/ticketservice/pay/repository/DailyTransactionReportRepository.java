package com.example.ticketservice.pay.repository;

import com.example.ticketservice.pay.entity.member.DailyTransactionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyTransactionReportRepository extends JpaRepository<DailyTransactionReport, Long> {
}
