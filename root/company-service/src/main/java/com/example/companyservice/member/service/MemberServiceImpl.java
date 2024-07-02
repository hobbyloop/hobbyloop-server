package com.example.companyservice.member.service;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.kafka.KafkaProducer;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.company.client.TicketServiceClient;
import com.example.companyservice.member.dto.MemberDeletedDto;
import com.example.companyservice.member.dto.MemberUpdatedDto;
import com.example.companyservice.member.dto.response.MemberDetailResponseDto;
import com.example.companyservice.member.dto.response.MemberInfoResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import com.example.companyservice.member.dto.response.MemberMyPageHomeResponseDto;
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
    private final TicketServiceClient ticketServiceClient;
    private final JwtUtils jwtUtils;
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public TokenResponseDto createMember(CreateMemberRequestDto requestDto) {
        Member member = Member.of(requestDto);
        if (memberRepository.existsByProviderAndSubject(member.getProvider(), member.getSubject())) {
            throw new ApiException(ExceptionEnum.DUPLICATE_MEMBER_EXCEPTION);
        }

        Member savedMember = memberRepository.save(member);

        ticketServiceClient.earnPointsWhenJoining(savedMember.getId()); // 회원가입 포인트 적립

        String accessToken = jwtUtils.createToken(member.getId(), member.getRole());
        String refreshToken = jwtUtils.createRefreshToken(member.getId(), member.getRole());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void updateMember(long memberId, MemberUpdateRequestDto request, MultipartFile profileImage) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (profileImage != null) {
            String profileImageKey = amazonS3Service.saveS3Img(profileImage, "MemberProfileImage");
            String profileImageUrl = amazonS3Service.getFileUrl(profileImageKey);

            member.update(request, profileImageKey, profileImageUrl);
            return;
        }

        member.update(request, member.getProfileImageKey(), member.getProfileImageUrl());

        kafkaProducer.send("update-centermembership-info", MemberUpdatedDto.from(member));
    }

    @Override
    @Transactional
    public void deleteMember(long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        member.delete();

        // TODO: CenterMembership에서도 지워야 되나?
        kafkaProducer.send("delete-member-points", new MemberDeletedDto(memberId));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        return MemberDetailResponseDto.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        return MemberInfoResponseDto.from(member);
    }

    @Override
    @Transactional
    public MemberMyPageHomeResponseDto myPageHome(long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        Long points = ticketServiceClient.getMyTotalPoints(memberId).getData();
        Long ticketCount = ticketServiceClient.getMyTicketCount(memberId).getData();
        Long couponCount = ticketServiceClient.getCountOfAvailableMemberCoupons(memberId).getData();

        return MemberMyPageHomeResponseDto.of(member, points, ticketCount, couponCount);
    }
}
