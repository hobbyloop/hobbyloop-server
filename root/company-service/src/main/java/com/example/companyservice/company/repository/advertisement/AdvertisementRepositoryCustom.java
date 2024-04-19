package com.example.companyservice.company.repository.advertisement;

import com.example.companyservice.company.entity.Advertisement;

import java.util.List;

public interface AdvertisementRepositoryCustom {

    List<Advertisement> findAllBannerAdvertisement();

    List<Advertisement> findAllCPCAdvertisement();

    List<Advertisement> findAllCPCCPMAdvertisement();

    List<Integer> getUsedRank();
}
