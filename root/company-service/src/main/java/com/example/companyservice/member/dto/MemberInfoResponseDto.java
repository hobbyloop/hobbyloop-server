package com.example.companyservice.member.dto;

import com.example.companyservice.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberInfoResponseDto {
    private String memberName;
    private String phoneNumber;
    private LocalDate birthday;
    private String gender;

    public static MemberInfoResponseDto from(Member member) {
        return MemberInfoResponseDto.builder()
                .memberName(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthday(member.getBirthday())
                .gender("남") // TODO: instructor 패키지의 Gender 수정 필요
                .build();
    }
}
