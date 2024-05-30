package com.example.ticketservice.ticket.client;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    // TODO: 추후 구현 필요
    @GetMapping("/api/v1/members/{memberId}")
    BaseResponseDto<MemberInfoResponseDto> getMemberInfo(@PathVariable(value = "memberId") long memberId);
}
