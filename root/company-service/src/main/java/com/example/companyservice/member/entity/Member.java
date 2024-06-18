package com.example.companyservice.member.entity;

import com.example.companyservice.company.entity.Role;
import com.example.companyservice.company.entity.TimeStamped;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Member extends TimeStamped {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String nickname;

    private int gender;

    private LocalDate birthday;

    private String phoneNumber;

    private String profileImageKey;

    private String profileImageUrl;

    private boolean isOption1;

    private boolean isOption2;

    private boolean isDeleted;

    private String provider;

    private String subject;

    private String ci;

    private String di;

    private String oauth2AccessToken;

    private Role role;

    public static Member of (CreateMemberRequestDto requestDto) {
        return Member.builder()
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .nickname(requestDto.getNickname())
            .gender(requestDto.getGender())
            .birthday(requestDto.getBirthday())
            .phoneNumber(requestDto.getPhoneNumber())
            .isOption1(requestDto.isOption1())
            .isOption2(requestDto.isOption2())
            .provider(requestDto.getProvider())
            .subject(requestDto.getSubject())
            .oauth2AccessToken(requestDto.getOauth2AccessToken())
            .ci(requestDto.getCi())
            .di(requestDto.getDi())
            .role(Role.USER)
            .isDeleted(false)
            .build();
    }

    public void deleteMember() {
        this.isDeleted = false;
    }

    public void update(MemberUpdateRequestDto request, String profileImageKey, String profileImageUrl) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.birthday = request.getBirthday();
        this.profileImageKey = profileImageKey;
        this.profileImageUrl = profileImageUrl;
    }
}
