package com.example.companyservice.company.repository.advertisement;

import com.example.companyservice.company.entity.AdvertisementLog;

import java.util.List;

public interface AdvertisementLogRepositoryCustom {

    List<AdvertisementLog> findAllByCenterIdBetweenYear(long centerId, int year);
}
