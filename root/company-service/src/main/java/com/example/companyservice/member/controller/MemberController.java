package com.example.companyservice.member.controller;

import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.service.MemberService;
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
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join/members")
    public ResponseEntity<BaseResponseDto<Long>> createMember(@RequestBody CreateMemberRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new BaseResponseDto<>(memberService.createMember(requestDto)));
    }

}
