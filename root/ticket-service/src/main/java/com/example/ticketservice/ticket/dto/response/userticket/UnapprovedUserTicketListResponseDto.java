package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.entity.UserTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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