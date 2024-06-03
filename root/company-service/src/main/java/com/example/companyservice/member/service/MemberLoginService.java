package com.example.companyservice.member.service;

import com.example.companyservice.member.dto.MemberLoginResponseDto;
import com.example.companyservice.member.dto.request.MemberLoginRequestDto;

public interface MemberLoginService {

    MemberLoginResponseDto login(MemberLoginRequestDto requestDto);
}
