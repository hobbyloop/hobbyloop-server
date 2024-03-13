package com.example.companyservice.service;

import com.example.companyservice.dto.request.AdvertisementRequestDto;

public interface AdvertisementService {

    Long createAdvertisement(long centerId, AdvertisementRequestDto requestDto);
}
