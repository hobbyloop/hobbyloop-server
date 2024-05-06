package com.example.companyservice.common.security;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.repository.CompanyRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    private final CompanyRepository companyRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("----------------------------");
        log.info("onAuthenticationSuccess");

        OAuthUserDetails authMember = (OAuthUserDetails)authentication.getPrincipal();

        Company company = companyRepository.findByEmail(authMember.getEmail())
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));

        String accessToken = jwtUtil.createToken(company.getId());
        String refreshToken = jwtUtil.createRefreshToken(company.getId());

        String url = UriComponentsBuilder.fromUriString("http://localhost/welcome")
                .queryParam("access-token", accessToken)
                .queryParam("refresh-token", refreshToken)
                .build()
                .toUri()
                .toString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
