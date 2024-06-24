package com.example.companyservice.member.service;

import com.example.companyservice.member.dto.response.MemberLoginResponseDto;
import com.example.companyservice.member.dto.request.MemberLoginRequestDto;

public interface MemberLoginService {

    MemberLoginResponseDto login(MemberLoginRequestDto requestDto);
}
