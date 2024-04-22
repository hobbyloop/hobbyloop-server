package com.example.ticketservice.service;

import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipDetailResponseDto;

import java.util.List;

public interface CenterMembershipService {
    void joinCenterMembership(Long centerId, Long memberId);
    List<CenterMemberResponseDto> getCenterMemberList(Long centerId, int pageNo, int sortId);
    CenterMembershipDetailResponseDto getCenterMembershipDetail(long centerMembershipId);
}
