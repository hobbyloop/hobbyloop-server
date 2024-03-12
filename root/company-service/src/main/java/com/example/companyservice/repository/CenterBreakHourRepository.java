package com.example.companyservice.repository;

import com.example.companyservice.entity.CenterBreakHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterBreakHourRepository extends JpaRepository<CenterBreakHour, Long> {

    List<CenterBreakHour> findAllByCenterId(long centerId);
}
