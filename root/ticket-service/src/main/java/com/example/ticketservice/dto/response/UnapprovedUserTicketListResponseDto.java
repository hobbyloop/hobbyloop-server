package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.entity.UserTicket;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UnapprovedUserTicketListResponseDto {
    private Long userTicketId;

    private String ticketName;

    private String memberName;

    private String phoneNumber;

    private LocalDateTime createdAt;

    public static UnapprovedUserTicketListResponseDto of(UserTicket userTicket, MemberInfoResponseDto memberInfo) {
        return UnapprovedUserTicketListResponseDto.builder()
                .userTicketId(userTicket.getId())
                .ticketName(userTicket.getTicket().getName())
                .memberName(memberInfo.getMemberName())
                .phoneNumber(memberInfo.getPhoneNumber())
                .createdAt(userTicket.getCreatedAt())
                .build();
    }
}
