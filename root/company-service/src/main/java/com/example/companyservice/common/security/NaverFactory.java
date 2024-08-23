package com.example.companyservice.common.security;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class NaverFactory implements OAuthFactory {

    @Override
    public String getUserInfoRequestUrl() {
        return "https://openapi.naver.com/v1/nid/me";
    }

    @Override
    public OAuthAttribute createOauthAttribute(Map<String, Object> attributes) {
        return OAuthAttribute.ofNaver(attributes);
    }
}
