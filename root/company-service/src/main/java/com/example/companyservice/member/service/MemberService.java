package com.example.companyservice.member.service;

import com.example.companyservice.member.dto.MemberDetailResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    Long createMember(CreateMemberRequestDto requestDto);

    void updateMember(long memberId, MemberUpdateRequestDto requestDto, MultipartFile profileImage);

    MemberDetailResponseDto getMemberDetail(long memberId);
}
