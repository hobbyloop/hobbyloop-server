package com.example.companyservice.company.controller;

import com.example.companyservice.company.dto.response.CenterDistanceInfoResponseDto;
import com.example.companyservice.company.dto.response.CenterInfoResponseDto;
import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.company.dto.response.OriginalBusinessResponseDto;
import com.example.companyservice.company.dto.response.OriginalCenterResponseDto;
import com.example.companyservice.company.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/centers")
public class CenterClientController {
    private final CenterService centerService;

    @GetMapping("/info/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterInfoResponseDto>> getCenterInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfo(centerId)));
    }

    @GetMapping("/original/{centerId}")
    public ResponseEntity<BaseResponseDto<OriginalCenterResponseDto>> getOriginalCenterInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getOriginalCenterInfo(centerId)));
    }

    @GetMapping("/original/business/{centerId}")
    public ResponseEntity<BaseResponseDto<OriginalBusinessResponseDto>> getOriginalBusinessInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getOriginalBusinessInfo(centerId)));
    }

    @GetMapping("/distance/{centerId}/{memberId}/{refundable}/{allow-location}/{latitude}/{longitude}")
    public ResponseEntity<BaseResponseDto<CenterDistanceInfoResponseDto>> getCenterDistanceInfo(@PathVariable(value = "centerId") long centerId,
                                                                                                @PathVariable(value = "memberId") long memberId,
                                                                                                @PathVariable(value = "refundable") int refundable,
                                                                                                @PathVariable(value = "allow-location") int allowLocation,
                                                                                                @PathVariable(value = "latitude", required = false) Double latitude,
                                                                                                @PathVariable(value = "longitude", required = false) Double longitude,
                                                                                                @RequestParam(value = "location") List<String> locations) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterDistanceInfo(centerId, memberId, refundable, allowLocation, latitude, longitude, locations)));
    }
}
