package com.example.companyservice.member.client;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.member.client.dto.PointEarnedResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ticket-service")
public interface PointServiceClient {

    @PostMapping("/api/v1/points/client/join/{memberId}")
    BaseResponseDto<PointEarnedResponseDto> join(@PathVariable Long memberId);
}
