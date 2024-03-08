package com.example.payservice.controller;

import com.example.payservice.dto.BaseResponseDto;
import com.example.payservice.dto.request.CompanyRatePlanRequestDto;
import com.example.payservice.service.CompanyRatePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CompanyRatePlanController {

    private final CompanyRatePlanService companyRatePlanService;

    @PostMapping("/company-rate-plan")
    public ResponseEntity<BaseResponseDto<Long>> createCompanyRatePlan(@RequestBody CompanyRatePlanRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(companyRatePlanService.createCompanyRatePlan(requestDto)));
    }
}
