package com.example.companyservice.common.security;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("------------------------------");
        log.info("userRequest : {}", userRequest);

        String provider = userRequest.getClientRegistration().getClientName();

        log.info("provider : {}", provider);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==============================");
        oAuth2User.getAttributes().forEach((k,v) -> log.info("{} : {}", k, v));

        String oauth2AccessToken = userRequest.getAccessToken().getTokenValue();
        log.info("oauth2AccessToken : {}", oauth2AccessToken);

        String subject;
        String email;
        if (provider.equals("Kakao")) {
            subject = String.valueOf(oAuth2User.getAttributes().get("id"));
            HashMap<String, String> map = oAuth2User.getAttribute("kakao_account");
            email = map.get("email");
        } else if (provider.equals("Naver")) {
            HashMap<String, String> map = oAuth2User.getAttribute("response");
            subject = map.get("id");
            email = map.get("email");
        } else if (provider.equals("Google")) {
            subject = oAuth2User.getAttribute("sub");
            email = oAuth2User.getAttribute("email");
        } else {
            throw new ApiException(ExceptionEnum.NOT_SUPPORT_PROVIDER_TYPE);
        }

        log.info("subject : {}", subject);
        log.info("EMAIL : {}", email);

        String password = UUID.randomUUID().toString();

        return new OAuthUserDetails(
                email,
                new BCryptPasswordEncoder().encode(password),
                Collections.emptySet(),
                oAuth2User.getAttributes(),
                provider,
                subject,
                oauth2AccessToken
        );
    }
}