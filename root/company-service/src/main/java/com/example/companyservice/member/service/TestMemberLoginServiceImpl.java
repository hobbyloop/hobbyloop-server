package com.example.companyservice.member.service;


import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.member.dto.request.TestMemberLoginRequestDto;
import com.example.companyservice.member.dto.response.MemberLoginResponseDto;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestMemberLoginServiceImpl {

    private final MemberRepository memberRepository;

    private final JwtUtils jwtUtils;

    public MemberLoginResponseDto login(TestMemberLoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String provider = requestDto.getProvider();
        String subject = requestDto.getSubject();

        Member member = memberRepository.findByEmailAndProviderAndSubject(email, provider, subject)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        String accessToken = jwtUtils.createToken(member.getId(), member.getRole());
        String refreshToken = jwtUtils.createRefreshToken(member.getId(), member.getRole());

        return MemberLoginResponseDto.of(accessToken,
                                        refreshToken,
                                        email,
                                        provider,
                                        subject,
                                        "test");
    }
}
