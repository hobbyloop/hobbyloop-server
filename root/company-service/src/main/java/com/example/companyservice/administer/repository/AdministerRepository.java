package com.example.companyservice.administer.repository;

import com.example.companyservice.administer.entity.Administer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministerRepository extends JpaRepository<Administer, Long> {
    Optional<Administer> findByEmail(String email);
}
