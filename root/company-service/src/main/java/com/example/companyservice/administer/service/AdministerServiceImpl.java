package com.example.companyservice.administer.service;

import com.example.companyservice.administer.dto.request.AdministerRequestDto;
import com.example.companyservice.administer.dto.response.LoginResponseDto;
import com.example.companyservice.administer.entity.Administer;
import com.example.companyservice.administer.repository.AdministerRepository;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdministerServiceImpl implements AdministerService {

    private final AdministerRepository administerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Long join(AdministerRequestDto requestDto) {
        String email = requestDto.getEmail();
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Administer administer = Administer.of(email, encodedPassword);
        administerRepository.save(administer);
        return administer.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(AdministerRequestDto requestDto) {
        Administer administer = administerRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOT_EXIST_EMAIL_EXCEPTION));

        if (!passwordEncoder.matches(requestDto.getPassword(), administer.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCH_EXCEPTION);
        }

        String accessToken = jwtUtils.createToken(administer.getId());
        String refreshToken = jwtUtils.createRefreshToken(administer.getId());
        return LoginResponseDto.of(accessToken, refreshToken);
    }
}
