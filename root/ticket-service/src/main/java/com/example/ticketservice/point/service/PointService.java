package com.example.ticketservice.point.service;

import com.example.ticketservice.pay.entity.member.vo.PointUsage;
import com.example.ticketservice.point.dto.PointEarnedResponseDto;
import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.example.ticketservice.point.policy.PlatformPointPolicy;

import java.util.List;

public interface PointService {
    PointEarnedResponseDto earnPointWhenJoining(Long memberId);

    PointEarnedResponseDto earnPointGeneral(Long memberId, PlatformPointPolicy pointPolicy);

    PointEarnedResponseDto earnPointSpecificCompany(Long memberId, Long companyId, PlatformPointPolicy pointPolicy);

    Long getMemberTotalPoints(Long memberId);

    PointHistoryListResponseDto getPointHistory(Long memberId);

    PointHistoryListResponseDto getExpiringSoonPointHistory(Long memberId);

    void earnPointWhenPurchase(Long memberId, Long companyId, Long centerId, Long totalAmount);

    void usePointWhenPurchase(Long memberId, List<PointUsage> pointUsages, String orderName);
}
