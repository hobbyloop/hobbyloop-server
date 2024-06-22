package com.example.companyservice.company.controller;

import com.example.companyservice.company.dto.response.IsBookmarkResponseDto;
import com.example.companyservice.company.dto.response.CenterInfoResponseDto;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/distance/{centerId}/{memberId}")
    public ResponseEntity<BaseResponseDto<IsBookmarkResponseDto>> getIsBookmark(@PathVariable(value = "centerId") long centerId, @PathVariable(value = "memberId") long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getIsBookmark(centerId, memberId)));
    }

    @GetMapping("/{centerId}/company")
    public ResponseEntity<BaseResponseDto<Long>> getCompanyIdOfCenter(@PathVariable(value = "centerId") Long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCompanyIdOfCenter(centerId)));
    }
}
