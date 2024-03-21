package com.example.companyservice.repository;

import com.example.companyservice.entity.QuickButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuickButtonRepository extends JpaRepository<QuickButton, Long> {

    @Query("select qb.buttonId from QuickButton qb where qb.center.id = :centerId")
    List<Integer> findAllButtonIdByCenterId(long centerId);

    void deleteAllByCenterId(long centerId);
}
