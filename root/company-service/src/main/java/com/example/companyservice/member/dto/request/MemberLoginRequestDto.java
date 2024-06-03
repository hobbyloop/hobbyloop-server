package com.example.companyservice.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDto {

    private String accessToken;

    private String provider;
}
