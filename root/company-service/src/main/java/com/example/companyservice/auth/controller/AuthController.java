package com.example.companyservice.auth.controller;

import com.example.companyservice.auth.dto.request.AdminLoginRequestDto;
import com.example.companyservice.auth.dto.request.MemberLoginRequestDto;
import com.example.companyservice.auth.dto.response.MemberLoginResponseDto;
import com.example.companyservice.auth.service.AuthService;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/members")
    public ResponseEntity<BaseResponseDto<MemberLoginResponseDto>> memberLogin(@RequestBody MemberLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(authService.memberLogin(requestDto)));
    }

    @PostMapping("/login/admins")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> adminLogin(@RequestBody @Valid AdminLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(authService.adminLogin(requestDto)));
    }

    @GetMapping("/refresh-access-token")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> refreshAccessToken(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(authService.refreshAccessToken(request)));
    }
}
