package com.example.companyservice.company.service;

import com.example.companyservice.company.dto.request.AdvertisementRequestDto;
import com.example.companyservice.company.dto.response.AdvertisementResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertisementService {

    Long createAdvertisement(long centerId, AdvertisementRequestDto requestDto, MultipartFile bannerImage);

    List<AdvertisementResponseDto> getAdvertisementList();

    List<Integer> getUsedRank();
}
