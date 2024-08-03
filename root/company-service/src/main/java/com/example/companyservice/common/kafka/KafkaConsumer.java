package com.example.companyservice.common.kafka;

import com.example.companyservice.admin.entity.BlindRequest;
import com.example.companyservice.admin.repository.BlindRequestRepository;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.client.dto.request.BlindReviewRequestDto;
import com.example.companyservice.company.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.companyservice.company.entity.*;
import com.example.companyservice.company.repository.*;
import com.example.companyservice.company.repository.advertisement.AdvertisementLogRepository;
import com.example.companyservice.company.repository.advertisement.AdvertisementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    private final AmazonS3Service amazonS3Service;

    private final CenterRepository centerRepository;

    private final CenterOperatingHourRepository centerOperatingHourRepository;

    private final CenterBreakHourRepository centerBreakHourRepository;

    private final CenterImageRepository centerImageRepository;

    private final BlindRequestRepository blindRequestRepository;

    private final AdvertisementLogRepository advertisementLogRepository;

    private final AdvertisementRepository advertisementRepository;

    @KafkaListener(topics = "update-address-info-success")
    @Transactional
    public void updateAddressInfoSuccess(String kafkaMessage) throws JsonProcessingException {
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        CenterOriginalAndUpdateInfoDto requestDto = objectMapper.readValue(kafkaMessage, CenterOriginalAndUpdateInfoDto.class);
        amazonS3Service.delete(requestDto.getOriginalLogoImageKey());

        List<CenterImage> oldCenterImageList = centerImageRepository.findAllByCenterIdAndIsDeletedTrue(requestDto.getCenterId());
        oldCenterImageList.forEach(i -> {
            centerImageRepository.delete(i);
            amazonS3Service.delete(i.getCenterImageKey());
        });
    }

    @KafkaListener(topics = "update-address-info-fail")
    @Transactional
    public void updateAddressInfoFail(String kafkaMessage) throws JsonProcessingException {
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        CenterOriginalAndUpdateInfoDto requestDto = objectMapper.readValue(kafkaMessage, CenterOriginalAndUpdateInfoDto.class);
        amazonS3Service.delete(requestDto.getNewLogoImageKey());

        Center center = centerRepository.findById(requestDto.getCenterId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        center.rollbackUpdate(requestDto);

        centerOperatingHourRepository.deleteAllByCenterId(requestDto.getCenterId());
        List<CenterOperatingHour> centerOperatingHourList = requestDto.getOperatingHourList().stream().map(o -> CenterOperatingHour.of(o, center)).toList();
        centerOperatingHourRepository.saveAll(centerOperatingHourList);

        centerBreakHourRepository.deleteAllByCenterId(requestDto.getCenterId());
        List<CenterBreakHour> centerBreakHourList = requestDto.getBreakHourList().stream().map(b -> CenterBreakHour.of(b, center)).toList();
        centerBreakHourRepository.saveAll(centerBreakHourList);

        List<String> oldCenterImageKeyList = requestDto.getOldCenterImageKeyList();
        oldCenterImageKeyList.forEach(k -> {
            Optional<CenterImage> centerImageKey = centerImageRepository.findByCenterImageKey(k);
            centerImageKey.ifPresent(centerImage -> centerImage.updateIsDeleted(false));
        });

        List<String> newCenterImageKeyList = requestDto.getNewCenterImageKeyList();
        newCenterImageKeyList.forEach(k -> {
            Optional<CenterImage> centerImageKey = centerImageRepository.findByCenterImageKey(k);
            if (centerImageKey.isPresent()) {
                amazonS3Service.delete(centerImageKey.get().getCenterImageKey());
                centerImageRepository.delete(centerImageKey.get());
            }
        });
    }

    @KafkaListener(topics = "blind-review-fail")
    @Transactional
    public void blindReviewFail(String kafkaMessage) throws JsonProcessingException {
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        BlindReviewRequestDto requestDto = objectMapper.readValue(kafkaMessage, BlindReviewRequestDto.class);
        Optional<BlindRequest> blindRequestOptional = blindRequestRepository.findById(requestDto.getBlindRequestId());
        if (blindRequestOptional.isPresent()) {
            BlindRequest blindRequest = blindRequestOptional.get();
            blindRequestRepository.delete(blindRequest);
        }
    }

    @KafkaListener(topics = "save-advertisement-select-log")
    @Transactional
    public void saveAdvertisementSelectLog(String kafkaMessage) throws JsonProcessingException {
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        List<Long> advertisementIdList = Collections.singletonList(objectMapper.readValue(kafkaMessage, Long.class));
        advertisementIdList.forEach(id -> {
            Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
            if (advertisementOptional.isPresent()) {
                Advertisement advertisement = advertisementOptional.get();
                AdvertisementLog advertisementLog = AdvertisementLog.of(advertisement, "select");
                advertisementLogRepository.save(advertisementLog);
            }
        });
    }

    @KafkaListener(topics = "save-advertisement-click-log")
    @Transactional
    public void saveAdvertisementClickLog(String kafkaMessage) throws JsonProcessingException {
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        Long advertisementId = objectMapper.readValue(kafkaMessage, Long.class);
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(advertisementId);
        if (advertisementOptional.isPresent()) {
            Advertisement advertisement = advertisementOptional.get();
            AdvertisementLog advertisementLog = AdvertisementLog.of(advertisement, "click");
            advertisementLogRepository.save(advertisementLog);
        }
    }
}
