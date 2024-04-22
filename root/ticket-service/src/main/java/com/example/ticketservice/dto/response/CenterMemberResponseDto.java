package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.entity.CenterMembership;
import com.example.ticketservice.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.entity.Ticket;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CenterMemberResponseDto {
    private long centerMembershipId;
    private String memberName;
    private String phoneNumber;
    private String status;
    private String ticketName;

    public static CenterMemberResponseDto of(CenterMembership centerMembership, Ticket ticket) {
        return CenterMemberResponseDto.builder()
                .centerMembershipId(centerMembership.getId())
                .memberName(centerMembership.getMemberName())
                .phoneNumber(centerMembership.getPhoneNumber())
                .status(CenterMembershipStatusEnum.findByValue(centerMembership.getStatus()).getName())
                .ticketName(ticket.getName())
                .build();
    }
}
