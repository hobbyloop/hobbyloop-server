package com.example.ticketservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CenterMembershipJoinRequestDto {
    private Long ticketId;
    private String memberName;
    private String phoneNumber;
    private String gender;
    private String birthday;
    private String email;
}
