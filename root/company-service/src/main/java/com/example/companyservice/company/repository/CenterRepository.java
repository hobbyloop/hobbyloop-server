package com.example.companyservice.company.repository;

import com.example.companyservice.company.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterRepository extends JpaRepository<Center, Long> {

    List<Center> findAllByCompanyId(long companyId);
}
