package com.example.companyservice.company.repository;

import com.example.companyservice.company.entity.CenterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CenterImageRepository extends JpaRepository<CenterImage, Long> {

    List<CenterImage> findAllByCenterId(long centerId);

    @Query("select ci.centerImageUrl from CenterImage ci where ci.center.id = :centerId")
    List<String> findAllCenterImage(@Param(value = "centerId") long centerId);
}
