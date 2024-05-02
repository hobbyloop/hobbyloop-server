package com.example.companyservice.common.security;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.util.CookieUtils;
import com.example.companyservice.common.util.JwtUtils;
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

    private final JwtUtils jwtUtils;

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
            String subject = authMember.getSubject();
            String oauth2AccessToken = authMember.getOauth2AccessToken();

            String state = cookieState.get();
            log.info("state : {}", state);

            if ("company".equals(state)) {
                Optional<Company> optionalCompany = companyRepository.findByProviderAndSubject(provider, subject);
                if (optionalCompany.isPresent()) {
                    Company company = optionalCompany.get();
                    String accessToken = jwtUtils.createToken(company.getId());
                    String refreshToken = jwtUtils.createRefreshToken(company.getId());
                    sendToken(request, response, accessToken, refreshToken, null, null, null, null);
                } else {
                    sendToken(request, response, null, null, email, provider, subject, oauth2AccessToken);
                }
            } else if ("instructor".equals(state)) {
                Optional<Instructor> instructor = instructorRepository.findByEmail(authMember.getEmail());
            } else {

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