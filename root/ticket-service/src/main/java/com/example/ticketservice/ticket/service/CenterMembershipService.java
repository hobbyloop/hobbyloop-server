package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.dto.request.CenterMembershipJoinRequestDto;
import com.example.ticketservice.ticket.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.ticket.dto.response.CenterMembershipDetailResponseDto;
import com.example.ticketservice.ticket.dto.response.CenterMembershipJoinedResponseDto;

import java.util.List;

public interface CenterMembershipService {
    void joinCenterMembership(Long centerId, Long memberId);
    CenterMembershipJoinedResponseDto joinCenterMembershipByAdmin(Long centerId, Long memberId, CenterMembershipJoinRequestDto request);
    List<CenterMemberResponseDto> getCenterMemberList(Long centerId, int pageNo, int sortId);
    CenterMembershipDetailResponseDto getCenterMembershipDetail(long centerMembershipId);
}
