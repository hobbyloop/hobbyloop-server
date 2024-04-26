package com.example.ticketservice.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponseDto {
    private String memberName;
    private String phoneNumber;
    private LocalDate birthday;
    private String gender;
}
