package com.example.companyservice.company.repository;

import com.example.companyservice.company.entity.QuickButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuickButtonRepository extends JpaRepository<QuickButton, Long> {

    @Query("select qb.buttonId from QuickButton qb where qb.center.id = :centerId")
    List<Integer> findAllButtonIdByCenterId(@Param(value = "centerId") long centerId);

    void deleteAllByCenterId(long centerId);
}
