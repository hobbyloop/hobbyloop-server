package com.example.companyservice.member.dto.request;

import lombok.Getter;

@Getter
public class TestMemberLoginRequestDto {
    private String email;
    private String provider;
    private String subject;
}
