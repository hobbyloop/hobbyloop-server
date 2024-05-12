package com.example.companyservice.company.controller;

import com.example.companyservice.company.dto.request.AdvertisementRequestDto;
import com.example.companyservice.company.service.AdvertisementService;
import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.company.dto.response.AdvertisementResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/advertisement/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> createAdvertisement(@PathVariable(value = "centerId") long centerId,
                                                                     @RequestPart(value = "requestDto") AdvertisementRequestDto requestDto,
                                                                     @RequestPart(required = false, value = "bannerImage") MultipartFile bannerImage) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(advertisementService.createAdvertisement(centerId, requestDto, bannerImage)));
    }

    @GetMapping("/advertisement")
    public ResponseEntity<BaseResponseDto<List<AdvertisementResponseDto>>> getAdvertisementList() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(advertisementService.getAdvertisementList()));
    }

    @GetMapping("/advertisement/rank")
    public ResponseEntity<BaseResponseDto<List<Integer>>> getUsedRank() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(advertisementService.getUsedRank()));
    }
}
