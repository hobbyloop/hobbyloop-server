package com.example.companyservice.administer.service;

import com.example.companyservice.administer.dto.request.AdministerRequestDto;
import com.example.companyservice.administer.dto.response.LoginResponseDto;

public interface AdministerService {

    Long join(AdministerRequestDto requestDto);

    LoginResponseDto login(AdministerRequestDto requestDto);
}
