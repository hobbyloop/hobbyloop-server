package com.example.companyservice.member.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.member.dto.MemberInfoResponseDto;
import com.example.companyservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/client")
public class MemberClientController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<BaseResponseDto<MemberInfoResponseDto>> getMemberInfo(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.getMemberInfo(memberId)));
    }
}
