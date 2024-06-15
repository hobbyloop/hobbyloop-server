package com.example.ticketservice.point.service;

import com.example.ticketservice.point.dto.PointEarnedResponseDto;
import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.example.ticketservice.point.policy.PlatformPointPolicy;

public interface PointService {
    PointEarnedResponseDto earnPointWhenJoining(Long memberId);

    PointEarnedResponseDto earnPointGeneral(Long memberId, PlatformPointPolicy pointPolicy);

    PointEarnedResponseDto earnPointSpecificCompany(Long memberId, Long companyId, PlatformPointPolicy pointPolicy);

    Long getMemberTotalPoints(Long memberId);

    PointHistoryListResponseDto getPointHistory(Long memberId);
}
