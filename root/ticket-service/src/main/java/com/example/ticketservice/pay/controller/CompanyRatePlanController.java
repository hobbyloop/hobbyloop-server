package com.example.ticketservice.pay.controller;

import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.pay.dto.request.CompanyRatePlanRequestDto;
import com.example.ticketservice.pay.service.CompanyRatePlanService;
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
