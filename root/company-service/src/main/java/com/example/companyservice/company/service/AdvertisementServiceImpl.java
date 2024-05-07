package com.example.companyservice.company.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.client.TicketServiceClient;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final CenterRepository centerRepository;

    private final AdvertisementRepository advertisementRepository;

    private final AmazonS3Service amazonS3Service;

    private final TicketServiceClient ticketServiceClient;

    @Override
    @Transactional
    public Long createAdvertisement(long centerId, AdvertisementRequestDto requestDto, MultipartFile bannerImage) {
        boolean hasTicket = ticketServiceClient.getHasTicket(centerId).getData();
        if (!hasTicket) {
            throw new ApiException(ExceptionEnum.NOT_HAS_TICKET_EXCEPTION);
        }

        if ("배너광고".equals(requestDto.getAdType())) {
            boolean existsByAdRank = advertisementRepository.existsByAdRank(requestDto.getAdRank());
            if (existsByAdRank) {
                throw new ApiException(ExceptionEnum.RANK_ALREADY_EXIST_EXCEPTION);
            } else {
                if (bannerImage == null) {
                    throw new ApiException(ExceptionEnum.BANNER_NULL_POINTER_EXCEPTION);
                }
            }
        }

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
