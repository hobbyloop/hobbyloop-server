package com.example.companyservice.company.repository;

import com.example.companyservice.company.entity.CenterBreakHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterBreakHourRepository extends JpaRepository<CenterBreakHour, Long> {

    List<CenterBreakHour> findAllByCenterId(long centerId);

    void deleteAllByCenterId(long centerId);
}
