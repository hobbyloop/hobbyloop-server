package com.example.companyservice.instructor.dto.request;

import com.example.companyservice.auth.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateInstructorRequestDto {

    private String email;

    private String provider;

    private String subject;

    private String oauth2AccessToken;

    private String ci;

    private String di;

    @JsonProperty
    private boolean isOption1;

    @JsonProperty
    private boolean isOption2;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    private int gender;

    private String phoneNumber;
}
