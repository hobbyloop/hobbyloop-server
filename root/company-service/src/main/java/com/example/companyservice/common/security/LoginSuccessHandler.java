package com.example.companyservice.common.security;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.util.CookieUtils;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.entity.Role;
import com.example.companyservice.company.repository.CompanyRepository;
import com.example.companyservice.instructor.domain.Instructor;
import com.example.companyservice.instructor.infrastructure.persistence.InstructorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    private final CompanyRepository companyRepository;

    private final InstructorRepository instructorRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("----------------------------");
        log.info("onAuthenticationSuccess");

        Optional<String> cookieState = CookieUtils.getCookie(request, "state")
                .map(Cookie::getValue);

        if (cookieState.isPresent() && StringUtils.isNotEmpty(cookieState.get())) {
            OAuthUserDetails authMember = (OAuthUserDetails)authentication.getPrincipal();
            String email = authMember.getEmail();
            String provider = authMember.getProvider();
            String providerId = authMember.getProviderId();

            log.info("state : {}", cookieState.get());
            String state = cookieState.get();

            if ("company".equals(state)) {
                Optional<Company> optionalCompany = companyRepository.findByEmail(email);
                Company company;
                if (optionalCompany.isPresent()) {
                    company = optionalCompany.get();
                } else {
                    company = Company.from(email, provider, providerId);
                    companyRepository.save(company);
                }
                sendToken(request, response, company.getId(), provider, company.getCi());
            } else if ("instructor".equals(state)) {
                Optional<Instructor> instructor = instructorRepository.findByEmail(authMember.getEmail());
            } else {

            }
        } else {
            throw new ApiException(ExceptionEnum.LOGIN_FAIL_EXCEPTION);
        }
    }

    private void sendToken(HttpServletRequest request, HttpServletResponse response, Long id, String provider, String ci) throws IOException {
        String accessToken = jwtUtil.createToken(id);
        String refreshToken = jwtUtil.createRefreshToken(id);

        String url = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/" + provider + "/callback")
                .queryParam("access-token", accessToken)
                .queryParam("refresh-token", refreshToken)
                .queryParam("first", StringUtils.isNotEmpty(ci) ? ci : null)
                .build()
                .toUri()
                .toString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }
}