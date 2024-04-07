package com.example.companyservice.controller;

import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.request.AdvertisementRequestDto;
import com.example.companyservice.dto.request.LocationRequestDto;
import com.example.companyservice.dto.response.AdvertisementResponseDto;
import com.example.companyservice.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/advertisement/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> createAdvertisement(@PathVariable long centerId,
                                                                     @RequestBody AdvertisementRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(advertisementService.createAdvertisement(centerId, requestDto)));
    }

    @GetMapping("/advertisement")
    public ResponseEntity<BaseResponseDto<List<AdvertisementResponseDto>>> getAdvertisementList(@RequestBody LocationRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(advertisementService.getAdvertisementList(requestDto)));
    }
}
