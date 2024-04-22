package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.entity.Ticket;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CenterMembershipDetailResponseDto {
    private long centerMembershipId;
    private String memberName;
    private LocalDate birthday;
    private String gender;
    private String phoneNumber;
    private List<TicketResponseDto> tickets;

    public static CenterMembershipDetailResponseDto of(long id, MemberInfoResponseDto memberInfo, List<TicketResponseDto> tickets) {
        return CenterMembershipDetailResponseDto.builder()
                .centerMembershipId(id)
                .memberName(memberInfo.getMemberName())
                .birthday(memberInfo.getBirthday())
                .gender(memberInfo.getGender())
                .phoneNumber(memberInfo.getPhoneNumber())
                .tickets(tickets)
                .build();
    }
}
