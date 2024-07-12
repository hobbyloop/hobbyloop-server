package com.example.companyservice.company.repository.company;

import com.example.companyservice.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom {

    Optional<Company> findByProviderAndSubjectAndIsDeleteFalse(String provider, String subject);
}
