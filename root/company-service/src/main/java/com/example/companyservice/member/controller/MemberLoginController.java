package com.example.companyservice.member.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.member.dto.MemberLoginResponseDto;
import com.example.companyservice.member.dto.request.MemberLoginRequestDto;
import com.example.companyservice.member.service.MemberLoginService;
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
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    @PostMapping("/login/members")
    public ResponseEntity<BaseResponseDto<MemberLoginResponseDto>> login(@RequestBody MemberLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberLoginService.login(requestDto)));
    }
}
