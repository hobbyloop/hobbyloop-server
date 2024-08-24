package com.example.companyservice.common.security;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class GoogleFactory implements OAuthFactory {

    @Override
    public String getUserInfoRequestUrl() {
        return "https://www.googleapis.com/oauth2/v2/userinfo";
    }

    @Override
    public OAuthAttribute createOauthAttribute(Map<String, Object> attributes) {
        return OAuthAttribute.ofGoogle(attributes);
    }
}
