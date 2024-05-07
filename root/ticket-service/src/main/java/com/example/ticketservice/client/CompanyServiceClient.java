package com.example.ticketservice.client;

import com.example.ticketservice.client.dto.response.CenterDistanceInfoResponseDto;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.client.dto.response.OriginalCenterResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {

    @GetMapping("/api/v1/centers/info/{centerId}")
    BaseResponseDto<CenterInfoResponseDto> getCenterInfo(@PathVariable(value = "centerId") long centerId);

    @GetMapping("/api/v1/admin/centers/original/{centerId}")
    BaseResponseDto<OriginalCenterResponseDto> getOriginalCenterInfo(@PathVariable(value = "centerId") long centerId);

    @GetMapping("/api/v1/admin/centers/original/business/{centerId}")
    BaseResponseDto<OriginalBusinessResponseDto> getOriginalBusinessInfo(@PathVariable(value = "centerId") long centerId);

    @GetMapping("/api/v1/centers/distance/{centerId}/{memberId}/{refundable}/{allow-location}/{latitude}/{longitude}")
    BaseResponseDto<CenterDistanceInfoResponseDto> getCenterDistanceInfo(@PathVariable(value = "centerId") long centerId,
                                                                         @PathVariable(value = "memberId") long memberId,
                                                                         @PathVariable(value = "refundable") int refundable,
                                                                         @PathVariable(value = "allow-location") int allowLocation,
                                                                         @PathVariable(value = "latitude", required = false) Double latitude,
                                                                         @PathVariable(value = "longitude", required = false) Double longitude,
                                                                         @RequestParam(value = "location") List<String> locations);
}
