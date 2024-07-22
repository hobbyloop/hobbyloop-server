package com.example.companyservice.admin.service;

import com.example.companyservice.auth.dto.request.AdminLoginRequestDto;

public interface AdminService {

    Long join(AdminLoginRequestDto requestDto);
}
