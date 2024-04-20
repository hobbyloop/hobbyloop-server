package com.example.companyservice.company.service;

import com.example.companyservice.company.common.exception.ApiException;
import com.example.companyservice.company.common.exception.ExceptionEnum;
import com.example.companyservice.company.dto.request.AdvertisementRequestDto;
import com.example.companyservice.company.repository.advertisement.AdvertisementRepository;
import com.example.companyservice.company.dto.response.AdvertisementResponseDto;
import com.example.companyservice.company.entity.Advertisement;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final CenterRepository centerRepository;

    private final AdvertisementRepository advertisementRepository;

    private final AmazonS3Service amazonS3Service;

    @Override
    @Transactional
    public Long createAdvertisement(long centerId, AdvertisementRequestDto requestDto, MultipartFile bannerImage) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        String bannerImageKey = amazonS3Service.saveS3Img(bannerImage, "BannerImage");
        String bannerImageUrl = amazonS3Service.getFileUrl(bannerImageKey);
        Advertisement advertisement = Advertisement.of(requestDto, center, bannerImageKey, bannerImageUrl);
        Advertisement saveAdvertisement = advertisementRepository.save(advertisement);
        return saveAdvertisement.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponseDto> getAdvertisementList() {
        List<Advertisement> advertisementList = advertisementRepository.findAllBannerAdvertisement();
        return advertisementList.stream().map(AdvertisementResponseDto::from).toList();
    }

    @Override
    @Transactional
    public List<Integer> getUsedRank() {
        return advertisementRepository.getUsedRank();
    }
}