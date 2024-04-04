package com.example.ticketservice.client;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.client.dto.response.OriginalCenterResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {

    @GetMapping("/api/v1//centers/info/{centerId}")
    BaseResponseDto<CenterInfoResponseDto> getCenterInfo(@PathVariable long centerId);

    @GetMapping("/api/v1//centers/original/{centerId}")
    BaseResponseDto<OriginalCenterResponseDto> getOriginalCenterInfo(@PathVariable long centerId);

    @GetMapping("/api/v1//centers/original/business/{centerId}")
    BaseResponseDto<OriginalBusinessResponseDto> getOriginalBusinessInfo(@PathVariable long centerId);
}
