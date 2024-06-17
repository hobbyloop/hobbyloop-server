package com.example.companyservice.member.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.util.Utils;
import com.example.companyservice.member.dto.MemberDetailResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import com.example.companyservice.member.dto.response.MemberMyPageHomeResponseDto;
import com.example.companyservice.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "사용자 API", description = "사용자, 마이페이지 관련 API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ResponseEntity<BaseResponseDto<Long>> createMember(@RequestBody CreateMemberRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new BaseResponseDto<>(memberService.createMember(requestDto)));
    }

    @PatchMapping
    @Operation(summary = "마이페이지 - 내 정보 수정", description = "내 정보 조회 호출 후 호출 가능")
    public ResponseEntity<BaseResponseDto<Void>> updateMember(
            @RequestPart(value = "requestDto") MemberUpdateRequestDto requestDto,
            @RequestPart(value = "profileImage") MultipartFile profileImage,
            HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        memberService.updateMember(memberId, requestDto, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>());
    }

    @GetMapping
    @Operation(summary = "마이페이지 - 내 정보 수정 화면에 필요한 정보 조회", description = "내 정보 수정을 위한 내 정보 조회")
    public ResponseEntity<BaseResponseDto<MemberDetailResponseDto>> getMemberDetail(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.getMemberDetail(memberId)));
    }

    @GetMapping("/my-page")
    @Operation(summary = "마이페이지 - 홈")
    public ResponseEntity<BaseResponseDto<MemberMyPageHomeResponseDto>> myPageHome(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.myPageHome(memberId)));
    }
}
