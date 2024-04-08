package com.example.ticketservice.client;

import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    BaseResponseDto<MemberInfoResponseDto> getMemberInfo(long memberId);
}
