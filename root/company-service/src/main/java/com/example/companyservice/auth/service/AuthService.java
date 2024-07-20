package com.example.companyservice.auth.service;

import com.example.companyservice.auth.dto.request.AdminLoginRequestDto;
import com.example.companyservice.auth.dto.request.MemberLoginRequestDto;
import com.example.companyservice.auth.dto.response.MemberLoginResponseDto;
import com.example.companyservice.common.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    MemberLoginResponseDto memberLogin(MemberLoginRequestDto requestDto);

    TokenResponseDto adminLogin(AdminLoginRequestDto requestDto);

    TokenResponseDto refreshAccessToken(HttpServletRequest request);
}
