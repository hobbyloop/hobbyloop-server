package com.example.companyservice.member.service;

import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createMember(CreateMemberRequestDto requestDto) {
        Member member = Member.of(requestDto);
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }
}
