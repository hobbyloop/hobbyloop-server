package com.example.companyservice.member.service;

import com.example.companyservice.member.dto.request.CreateMemberRequestDto;

public interface MemberService {
    Long createMember(CreateMemberRequestDto requestDto);
}
