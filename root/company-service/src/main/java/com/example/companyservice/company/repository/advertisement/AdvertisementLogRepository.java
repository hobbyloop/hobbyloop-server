package com.example.companyservice.company.repository.advertisement;

import com.example.companyservice.company.entity.AdvertisementLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementLogRepository extends JpaRepository<AdvertisementLog, Long>, AdvertisementLogRepositoryCustom {
}
