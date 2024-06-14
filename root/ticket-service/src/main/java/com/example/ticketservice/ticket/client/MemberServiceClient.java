package com.example.ticketservice.ticket.client;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @GetMapping("/api/v1/members/client/{memberId}")
    BaseResponseDto<MemberInfoResponseDto> getMemberInfo(@PathVariable(value = "memberId") long memberId);
}
