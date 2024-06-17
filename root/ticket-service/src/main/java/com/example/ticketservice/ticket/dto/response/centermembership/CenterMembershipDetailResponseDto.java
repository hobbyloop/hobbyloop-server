package com.example.ticketservice.ticket.dto.response.centermembership;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
