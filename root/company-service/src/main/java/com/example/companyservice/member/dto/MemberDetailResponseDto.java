package com.example.companyservice.member.dto;

import com.example.companyservice.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailResponseDto {
    private String name;
    private String nickname;
    private LocalDate birthday;
    private String phoneNumber;

    public static MemberDetailResponseDto from(Member member) {
        return MemberDetailResponseDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
