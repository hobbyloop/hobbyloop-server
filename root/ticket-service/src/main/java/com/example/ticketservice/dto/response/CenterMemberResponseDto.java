package com.example.ticketservice.dto.response;

import com.example.ticketservice.entity.CenterMembership;
import com.example.ticketservice.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
