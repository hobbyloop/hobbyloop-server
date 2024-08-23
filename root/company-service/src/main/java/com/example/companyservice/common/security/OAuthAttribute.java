package com.example.companyservice.common.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAttribute {

    private String email;

    private ProviderType providerType;

    private String subject;

    public static OAuthAttribute ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = String.valueOf(kakaoAccount.get("email"));
        String subject = String.valueOf(attributes.get("id"));

        return builder()
                .email(email)
                .providerType(ProviderType.KAKAO)
                .subject(subject)
                .build();
    }

    public static OAuthAttribute ofNaver(Map<String, Object> attributes) {
        String email = String.valueOf(attributes.get("email"));
        String subject = String.valueOf(attributes.get("id"));

        return builder()
                .email(email)
                .providerType(ProviderType.GOOGLE)
                .subject(subject)
                .build();
    }

    public static OAuthAttribute ofGoogle(Map<String, Object> attributes) {
        String email = String.valueOf(attributes.get("email"));
        String subject = String.valueOf(attributes.get("id"));

        return builder()
                .email(email)
                .providerType(ProviderType.GOOGLE)
                .subject(subject)
                .build();
    }
}