package com.example.companyservice.common.security;

import java.io.IOException;
import java.util.Optional;

import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.util.CookieUtils;
import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.repository.company.CompanyRepository;
import com.example.companyservice.instructor.infrastructure.persistence.InstructorRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    private final CompanyRepository companyRepository;

    private final InstructorRepository instructorRepository;

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("----------------------------");
        log.info("onAuthenticationSuccess");

        Optional<String> cookieState = CookieUtils.getCookie(request, "state").map(Cookie::getValue);

        if (cookieState.isPresent() && StringUtils.isNotEmpty(cookieState.get())) {
            OAuthUserDetails authMember = (OAuthUserDetails)authentication.getPrincipal();
            String email = authMember.getEmail();
            String provider = authMember.getProvider();
            String subject = authMember.getSubject();
            String oauth2AccessToken = authMember.getOauth2AccessToken();

            String state = cookieState.get();
            log.info("state : {}", state);

            if ("company".equals(state)) {
                Optional<Company> optionalCompany = companyRepository.findByProviderAndSubjectAndIsDeleteFalse(provider, subject);
                if (optionalCompany.isPresent()) {
                    Company company = optionalCompany.get();
                    String accessToken = jwtUtils.createToken(company.getId(), company.getRole());
                    String refreshToken = jwtUtils.createRefreshToken(company.getId(), company.getRole());
                    sendToken(request, response, accessToken, refreshToken, null, provider, null, null);
                } else {
                    sendToken(request, response, null, null, email, provider, subject, oauth2AccessToken);
                }
            } else if ("instructor".equals(state)) {

            }
        } else {
            throw new ApiException(ExceptionEnum.LOGIN_FAIL_EXCEPTION);
        }
    }

    private void sendToken(HttpServletRequest request,
                           HttpServletResponse response,
                           String accessToken,
                           String refreshToken,
                           String email,
                           String provider,
                           String subject,
                           String oauth2AccessToken) throws IOException {

        String url = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/" + provider + "/callback")
                .queryParam("access-token", accessToken)
                .queryParam("refresh-token", refreshToken)
                .queryParam("email", email)
                .queryParam("provider", provider)
                .queryParam("subject", subject)
                .queryParam("oauth2AccessToken", oauth2AccessToken)
                .build()
                .toUri()
                .toString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
