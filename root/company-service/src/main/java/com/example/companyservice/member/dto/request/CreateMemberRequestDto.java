package com.example.companyservice.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberRequestDto {
    @NotBlank
    @Parameter(description = "본명", required = true)
    private String name;

    @Parameter(description = "이메일", required = true)
    private String email;

    @Parameter(description = "닉네임", required = true)
    private String nickname;

    @Parameter(description = "1: 남자, 2: 여자", required = true)
    private int gender;

    @Parameter(description = "포맷: yyyy-MM-dd", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    @Parameter(description = "포맷: 010-1234-5678", required = true)
    private String phoneNumber;

    @JsonProperty
    @Parameter(description = "선택약관1", required = true)
    private boolean isOption1;

    @JsonProperty
    @Parameter(description = "선택약관2", required = true)
    private boolean isOption2;

    @Parameter(description = "OAuth2 프로바이더")
    private String provider;

    private String subject;

    @Parameter(description = "OAuth2 로그인 토큰(하비루프 발급 토큰 아님)", required = true)
    private String oauth2AccessToken;

    @Parameter(description = "뭐여")
    private String ci;

    @Parameter(description = "뭐여")
    private String di;
}
