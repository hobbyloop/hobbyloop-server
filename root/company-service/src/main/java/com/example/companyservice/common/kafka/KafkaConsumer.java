package com.example.companyservice.common.kafka;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.entity.CenterBreakHour;
import com.example.companyservice.company.entity.CenterImage;
import com.example.companyservice.company.entity.CenterOperatingHour;
import com.example.companyservice.company.repository.CenterBreakHourRepository;
import com.example.companyservice.company.repository.CenterImageRepository;
import com.example.companyservice.company.repository.CenterOperatingHourRepository;
import com.example.companyservice.company.repository.CenterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
