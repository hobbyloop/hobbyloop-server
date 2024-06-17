package com.example.companyservice.admin.service;

import com.example.companyservice.admin.dto.request.AdminRequestDto;
import com.example.companyservice.common.dto.TokenResponseDto;

public interface AdminService {

    Long join(AdminRequestDto requestDto);

    TokenResponseDto login(AdminRequestDto requestDto);
}
