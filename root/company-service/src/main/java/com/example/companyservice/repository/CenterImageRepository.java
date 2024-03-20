package com.example.companyservice.repository;

import com.example.companyservice.entity.CenterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CenterImageRepository extends JpaRepository<CenterImage, Long> {

    List<CenterImage> findAllByCenterId(long centerId);

    @Query("select ci.centerImageUrl from CenterImage ci where ci.center.id = :centerId")
    List<String> findAllCenterImage(long centerId);
}
