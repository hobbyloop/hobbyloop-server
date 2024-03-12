package com.example.companyservice.repository;

import com.example.companyservice.entity.QuickButton;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuickButtonRepository extends JpaRepository<QuickButton, Long> {

    List<QuickButton> findAllByCenterId(long centerId);

    void deleteAllByCenterId(long centerId);
}
