package com.example.companyservice.repository;

import com.example.companyservice.entity.CenterOperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterOperatingHourRepository extends JpaRepository<CenterOperatingHour, Long> {

    List<CenterOperatingHour> findAllByCenterId(long centerId);

    void deleteAllByCenterId(long centerId);
}
