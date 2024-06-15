package com.example.companyservice.company.client;

import com.example.companyservice.company.client.dto.request.CompanyRatePlanRequestDto;
import com.example.companyservice.company.client.dto.response.*;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.member.client.dto.PointEarnedResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ticket-service")
public interface TicketServiceClient {

    @GetMapping("/api/v1/client/tickets/{centerId}")
    BaseResponseDto<TicketClientBaseResponseDto> getTicketList(@PathVariable(value = "centerId") long centerId);

    @PostMapping("/api/v1/client/tickets/bookmark-ticket-list")
    BaseResponseDto<Map<Long, BookmarkScoreTicketResponseDto>> getBookmarkTicketList(@RequestBody List<Long> centerIdList);

    @GetMapping("/api/v1/client/tickets/detail/{centerId}")
    BaseResponseDto<TicketDetailClientResponseDto> getTicketDetailInfo(@PathVariable(value = "centerId") long centerId);

    @PostMapping("/api/v1/client/tickets/hot-ticket-list")
    BaseResponseDto<Map<Long, TicketInfoClientResponseDto>> getHotTicketList(@RequestBody List<Long> centerIdList);

    @PostMapping("/api/v1/client/tickets/recommend-ticket-list")
    BaseResponseDto<Map<Long, TicketInfoClientResponseDto>> getRecommendTicketList(@RequestBody List<Long> centerIdList);

    @PostMapping("/api/v1/company-rate-plan")
    BaseResponseDto<Long> createCompanyRatePlan(@RequestBody CompanyRatePlanRequestDto requestDto);

    @GetMapping("/api/v1/client/tickets/has-ticket/{centerId}")
    BaseResponseDto<Boolean> getHasTicket(@PathVariable(value = "centerId") long centerId);

    @PostMapping("/api/v1/points/client/join/{memberId}")
    BaseResponseDto<PointEarnedResponseDto> earnPointsWhenJoining(@PathVariable Long memberId);
}
