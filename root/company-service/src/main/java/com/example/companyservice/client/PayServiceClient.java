package com.example.companyservice.client;

import com.example.companyservice.client.dto.CompanyRatePlanRequestDto;
import com.example.companyservice.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pay-service")
public interface PayServiceClient {

    @PostMapping("/api/v1/company-rate-plan")
    BaseResponseDto<Long> createCompanyRatePlan(@RequestBody CompanyRatePlanRequestDto requestDto);
}
