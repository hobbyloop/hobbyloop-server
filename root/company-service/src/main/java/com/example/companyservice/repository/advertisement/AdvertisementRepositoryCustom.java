package com.example.companyservice.repository.advertisement;

import com.example.companyservice.dto.request.LocationRequestDto;
import com.example.companyservice.entity.Advertisement;

import java.util.List;

public interface AdvertisementRepositoryCustom {

    List<Advertisement> findAllAdvertisementAroundLocation();
}