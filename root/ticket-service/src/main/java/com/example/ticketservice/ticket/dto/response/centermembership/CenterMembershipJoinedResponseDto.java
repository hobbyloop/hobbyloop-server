package com.example.ticketservice.ticket.dto.response.centermembership;

import com.example.ticketservice.ticket.dto.request.CenterMembershipJoinRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CenterMembershipJoinedResponseDto {
    private String memberName;
    private String phoneNumber;
    private String gender;
    private String email;
    private String birthday;
    private String ticketName;

    public static CenterMembershipJoinedResponseDto of(CenterMembershipJoinRequestDto request, String ticketName) {
        return CenterMembershipJoinedResponseDto.builder()
                .memberName(request.getMemberName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .email(request.getEmail())
                .birthday(request.getBirthday())
                .ticketName(ticketName)
                .build();
    }
}