package com.example.ticketservice.ticket.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoResponseDto {
    private String memberName;
    private String nickname;
    private String phoneNumber;
    private LocalDate birthday;
    private String gender;
}
