package com.example.companyservice.company.repository.advertisement;

import com.example.companyservice.company.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {
    boolean existsByAdRank(int adRank);
}
