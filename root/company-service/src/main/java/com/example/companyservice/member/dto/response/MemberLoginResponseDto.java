package com.example.companyservice.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginResponseDto {

    private String accessToken;

    private String refreshToken;

    private String email;

    private String provider;

    private String subject;

    private String oauth2AccessToken;

    public static MemberLoginResponseDto of(String accessToken,
                                            String refreshToken,
                                            String email,
                                            String provider,
                                            String subject,
                                            String oauth2AccessToken) {
        return MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .provider(provider)
                .subject(subject)
                .oauth2AccessToken(oauth2AccessToken)
                .build();
    }
}