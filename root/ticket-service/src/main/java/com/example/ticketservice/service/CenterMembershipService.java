package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.CenterMembershipJoinRequestDto;
import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipDetailResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipJoinedResponseDto;

import java.util.List;

public interface CenterMembershipService {
    void joinCenterMembership(Long centerId, Long memberId);
    CenterMembershipJoinedResponseDto joinCenterMembershipByAdmin(Long centerId, Long memberId, CenterMembershipJoinRequestDto request);
    List<CenterMemberResponseDto> getCenterMemberList(Long centerId, int pageNo, int sortId);
    CenterMembershipDetailResponseDto getCenterMembershipDetail(long centerMembershipId);
}
