package com.example.companyservice.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberRequestDto {
    private String name;

    private String email;

    private String nickname;

    private int gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    private String phoneNumber;

    @JsonProperty
    private boolean isOption1;

    @JsonProperty
    private boolean isOption2;

    private String provider;

    private String subject;

    private String oauth2AccessToken;

    private String ci;

    private String di;
}
