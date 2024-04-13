package com.example.companyservice.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.dto.request.AdvertisementRequestDto;
import com.example.companyservice.dto.response.AdvertisementResponseDto;
import com.example.companyservice.entity.Advertisement;
import com.example.companyservice.entity.Center;
import com.example.companyservice.repository.advertisement.AdvertisementRepository;
import com.example.companyservice.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final CenterRepository centerRepository;

    private final AdvertisementRepository advertisementRepository;

    @Override
    @Transactional
    public Long createAdvertisement(long centerId, AdvertisementRequestDto requestDto) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        Advertisement advertisement = Advertisement.of(requestDto, center);
        Advertisement saveAdvertisement = advertisementRepository.save(advertisement);
        return saveAdvertisement.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponseDto> getAdvertisementList() {
        List<Advertisement> advertisementList = advertisementRepository.findAllBannerAdvertisement();
        return advertisementList.stream().map(AdvertisementResponseDto::from).toList();
    }
}
