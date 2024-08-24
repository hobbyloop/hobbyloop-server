package com.example.companyservice.common.security;

import java.util.Map;

public interface OAuthFactory {

    String getUserInfoRequestUrl();

    OAuthAttribute createOauthAttribute(Map<String, Object> map);
}
