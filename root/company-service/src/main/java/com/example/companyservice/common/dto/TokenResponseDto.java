package com.example.companyservice.common.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {

    @Parameter(description = "액세스 토큰, 만료 기한은 XX", required = true)
    private String accessToken;

    @Parameter(description = "리프레시 토큰, 만료 기한은 XX", required = true)
    private String refreshToken;

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
