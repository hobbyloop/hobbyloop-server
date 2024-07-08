package com.example.companyservice.company.repository;

import com.example.companyservice.company.entity.CenterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CenterImageRepository extends JpaRepository<CenterImage, Long> {

    List<CenterImage> findAllByCenterIdAndIsDeletedTrue(long centerId);

    @Query("select ci.centerImageUrl from CenterImage ci where ci.center.id = :centerId and ci.isDeleted = false")
    List<String> findAllCenterImage(@Param(value = "centerId") long centerId);

    @Modifying(clearAutomatically = true)
    @Query("update CenterImage ci set ci.isDeleted = true where ci.center.id = :centerId")
    void updateAllIsDeletedTrue(@Param(value = "centerId") long centerId);

    Optional<CenterImage> findByCenterImageKey(String centerImageKey);
}
