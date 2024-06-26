package com.example.companyservice.member.service;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.member.dto.response.MemberDetailResponseDto;
import com.example.companyservice.member.dto.response.MemberInfoResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import com.example.companyservice.member.dto.response.MemberMyPageHomeResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    TokenResponseDto createMember(CreateMemberRequestDto requestDto);

    void updateMember(long memberId, MemberUpdateRequestDto requestDto, MultipartFile profileImage);

    void deleteMember(long memberId);

    MemberDetailResponseDto getMemberDetail(long memberId);

    MemberInfoResponseDto getMemberInfo(long memberId);

    MemberMyPageHomeResponseDto myPageHome(long memberId);
}
