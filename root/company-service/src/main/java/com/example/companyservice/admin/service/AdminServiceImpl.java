package com.example.companyservice.admin.service;

import com.example.companyservice.admin.entity.Admin;
import com.example.companyservice.admin.repository.AdminRepository;
import com.example.companyservice.auth.dto.request.AdminLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long join(AdminLoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Admin admin = Admin.of(email, encodedPassword);
        adminRepository.save(admin);
        return admin.getId();
    }
}
