package com.example.ticketservice.client;

import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @GetMapping("/api/v1/members/{memberId}")
    BaseResponseDto<MemberInfoResponseDto> getMemberInfo(@PathVariable long memberId);
}
