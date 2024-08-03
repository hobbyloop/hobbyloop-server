package com.example.companyservice.instructor.entity;

import com.example.companyservice.auth.entity.Role;
import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.instructor.dto.request.CreateInstructorRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Instructor extends TimeStamped {

    @Id
    @Column(name = "instructor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String provider;

    private String subject;

    private String oauth2AccessToken;

    private String ci;

    private String di;

    private Role role;

    private boolean isOption1;

    private boolean isOption2;

    private String name;

    private LocalDate birthday;

    private int gender;

    private String phoneNumber;

    private boolean isDelete;

    public static Instructor from(CreateInstructorRequestDto requestDto) {
        return Instructor.builder()
                .email(requestDto.getEmail())
                .provider(requestDto.getProvider())
                .subject(requestDto.getSubject())
                .oauth2AccessToken(requestDto.getOauth2AccessToken())
                .ci(requestDto.getCi())
                .di(requestDto.getDi())
                .role(Role.INSTRUCTOR)
                .isOption1(requestDto.isOption1())
                .isOption2(requestDto.isOption2())
                .name(requestDto.getName())
                .birthday(requestDto.getBirthday())
                .gender(requestDto.getGender())
                .phoneNumber(requestDto.getPhoneNumber())
                .isDelete(false)
                .build();
    }
}
