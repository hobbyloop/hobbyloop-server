package com.example.companyservice.admin.service;

import com.example.companyservice.admin.dto.request.AdminRequestDto;
import com.example.companyservice.admin.dto.response.LoginResponseDto;

public interface AdminService {

    Long join(AdminRequestDto requestDto);

    LoginResponseDto login(AdminRequestDto requestDto);
}
