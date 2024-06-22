package com.example.ticketservice.ticket.client;

import com.example.ticketservice.ticket.client.dto.response.*;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {

    @GetMapping("/api/v1/centers/info/{centerId}")
    BaseResponseDto<CenterInfoResponseDto> getCenterInfo(@PathVariable(value = "centerId") long centerId);

    @GetMapping("/api/v1/admin/centers/original/{centerId}")
    BaseResponseDto<OriginalCenterResponseDto> getOriginalCenterInfo(@PathVariable(value = "centerId") long centerId);

    @GetMapping("/api/v1/admin/centers/original/business/{centerId}")
    BaseResponseDto<OriginalBusinessResponseDto> getOriginalBusinessInfo(@PathVariable(value = "centerId") long centerId);

    @GetMapping("/api/v1/centers/distance/{centerId}/{memberId}")
    BaseResponseDto<IsBookmarkResponseDto> getIsBookmark(@PathVariable(value = "centerId") long centerId, @PathVariable(value = "memberId") long memberId);

    @GetMapping("/{centerId}/company")
    BaseResponseDto<Long> getCompanyIdOfCenter(@PathVariable(value = "centerId") Long centerId);

    @GetMapping("/api/v1/members/client/{memberId}")
    BaseResponseDto<MemberInfoResponseDto> getMemberInfo(@PathVariable(value = "memberId") long memberId);
}
