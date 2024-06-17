package com.example.companyservice.admin.service;

import com.example.companyservice.admin.dto.request.AdminRequestDto;
import com.example.companyservice.admin.entity.Admin;
import com.example.companyservice.admin.repository.AdminRepository;
import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Long join(AdminRequestDto requestDto) {
        String email = requestDto.getEmail();
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Admin admin = Admin.of(email, encodedPassword);
        adminRepository.save(admin);
        return admin.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto login(AdminRequestDto requestDto) {
        Admin admin = adminRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOT_EXIST_EMAIL_EXCEPTION));

        if (!passwordEncoder.matches(requestDto.getPassword(), admin.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCH_EXCEPTION);
        }

        String accessToken = jwtUtils.createToken(admin.getId(), admin.getRole());
        String refreshToken = jwtUtils.createRefreshToken(admin.getId(), admin.getRole());
        return TokenResponseDto.of(accessToken, refreshToken);
    }
}
