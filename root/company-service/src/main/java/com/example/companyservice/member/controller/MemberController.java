package com.example.companyservice.member.controller;

import com.example.companyservice.auth.dto.response.MemberLoginResponseDto;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.security.RoleAuthorization;
import com.example.companyservice.common.swagger.ApiExceptionResponse;
import com.example.companyservice.common.util.Utils;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.TestMemberLoginRequestDto;
import com.example.companyservice.member.dto.response.MemberDetailResponseDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import com.example.companyservice.member.dto.response.MemberMyPageHomeResponseDto;
import com.example.companyservice.member.service.MemberService;
import com.example.companyservice.member.service.TestMemberLoginServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "회원 API", description = "회원 마이페이지 관련 API, 접근 권한: 회원(회원가입 API 제외)\n마이페이지의 포인트 내역, 보유 쿠폰은 가각 포인트 API, 쿠폰 API에 위치해있음.")
public class MemberController {

    private final MemberService memberService;

    private final TestMemberLoginServiceImpl testMemberLoginService;

    @GetMapping("/members")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "마이페이지 - 내 정보 수정 화면에 필요한 정보 조회", description = "내 정보 수정을 위한 내 정보 조회\n[피그마 링크](https://www.figma.com/file/nYEBH6aqCI37ZX0X6w7Ena?embed_host=notion&kind=file&mode=design&node-id=9914-16871&t=T0LzXHd8One1Acu9-0&type=design&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MemberDetailResponseDto.class)))
    public ResponseEntity<BaseResponseDto<MemberDetailResponseDto>> getMemberDetail(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.getMemberDetail(memberId)));
    }

    @PatchMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "마이페이지 - 내 정보 수정", description = "내 정보 조회 호출 후 호출 가능, 변경되지 않은 값들도 세팅해서 보내주세요\n(전화번호 인증은 추후 구현 예정)\n[피그마 링크](https://www.figma.com/file/nYEBH6aqCI37ZX0X6w7Ena?embed_host=notion&kind=file&mode=design&node-id=9914-16871&t=T0LzXHd8One1Acu9-0&type=design&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    public ResponseEntity<BaseResponseDto<Void>> updateMember(
            @RequestPart(value = "requestDto") MemberUpdateRequestDto requestDto,
            @RequestPart(value = "profileImage", required = false) @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile profileImage,
            HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);

        memberService.updateMember(memberId, requestDto, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>());
    }

    @GetMapping("/members/my-page")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "마이페이지 - 홈", description = "[피그마 링크](https://www.figma.com/file/nYEBH6aqCI37ZX0X6w7Ena?embed_host=notion&kind=file&m=dev&node-id=13430-22256&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MemberMyPageHomeResponseDto.class)))
    public ResponseEntity<BaseResponseDto<MemberMyPageHomeResponseDto>> myPageHome(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.myPageHome(memberId)));
    }

    @DeleteMapping("/members")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "회원 탈퇴")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Void>> deleteMember(HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);

        memberService.deleteMember(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입 후 바로 토큰 리턴")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> createMember(@RequestBody CreateMemberRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(memberService.createMember(requestDto)));
    }

    @PostMapping("/login/test")
    @Operation(summary = "테스트용 로그인", description = "카카오 인증 과정 없이 바로 토큰 리턴함")
    public ResponseEntity<BaseResponseDto<MemberLoginResponseDto>> loginTest(@RequestBody TestMemberLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(testMemberLoginService.login(requestDto)));
    }
}
