package com.example.companyservice.common.security;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class KakaoFactory implements OAuthFactory {

    @Override
    public String getUserInfoRequestUrl() {
        return "https://kapi.kakao.com/v2/user/me";
    }

    @Override
    public OAuthAttribute createOauthAttribute(Map<String, Object> attributes) {
        return OAuthAttribute.ofKakao(attributes);
    }
}
