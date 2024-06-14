package com.example.companyservice.member.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.member.client.PointServiceClient;
import com.example.companyservice.member.dto.MemberDetailResponseDto;
import com.example.companyservice.member.dto.MemberInfoResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;
    private final PointServiceClient pointServiceClient;

    @Override
    @Transactional
    public Long createMember(CreateMemberRequestDto requestDto) {
        Member member = Member.of(requestDto);
        Member savedMember = memberRepository.save(member);

        pointServiceClient.join(savedMember.getId()); // 회원가입 포인트 적립

        return savedMember.getId();
    }

    @Override
    @Transactional
    public void updateMember(long memberId, MemberUpdateRequestDto request, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (!profileImage.isEmpty()) {
            String profileImageKey = amazonS3Service.saveS3Img(profileImage, "MemberProfileImage");
            String profileImageUrl = amazonS3Service.getFileUrl(profileImageKey);

            member.update(request, profileImageKey, profileImageUrl);
            return;
        }

        member.update(request, member.getProfileImageKey(), member.getProfileImageUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        return MemberDetailResponseDto.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        return MemberInfoResponseDto.from(member);
    }
}
