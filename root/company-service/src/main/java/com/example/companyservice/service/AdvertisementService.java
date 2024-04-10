package com.example.companyservice.service;

import com.example.companyservice.dto.request.AdvertisementRequestDto;
import com.example.companyservice.dto.response.AdvertisementResponseDto;

import java.util.List;

public interface AdvertisementService {

    Long createAdvertisement(long centerId, AdvertisementRequestDto requestDto);

    List<AdvertisementResponseDto> getAdvertisementList();
}
