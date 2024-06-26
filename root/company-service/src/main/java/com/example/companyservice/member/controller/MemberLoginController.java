package com.example.companyservice.member.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.member.dto.request.TestMemberLoginRequestDto;
import com.example.companyservice.member.dto.response.MemberLoginResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberLoginRequestDto;
import com.example.companyservice.member.service.MemberLoginService;
import com.example.companyservice.member.service.MemberService;
import com.example.companyservice.member.service.TestMemberLoginServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "회원 로그인 API")
public class MemberLoginController {
    private final MemberLoginService memberLoginService;

    private final MemberService memberService;

    private final TestMemberLoginServiceImpl testMemberLoginService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입 후 바로 토큰 리턴")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> createMember(@RequestBody CreateMemberRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.createMember(requestDto)));
    }

    @PostMapping("/login/members")
    @Operation(summary = "로그인")
    public ResponseEntity<BaseResponseDto<MemberLoginResponseDto>> login(@RequestBody MemberLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberLoginService.login(requestDto)));
    }

    @PostMapping("/login/test")
    @Operation(summary = "테스트용 로그인", description = "카카오 인증 과정 없이 바로 토큰 리턴함")
    public ResponseEntity<BaseResponseDto<MemberLoginResponseDto>> loginTest(@RequestBody TestMemberLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(testMemberLoginService.login(requestDto)));
    }
}
