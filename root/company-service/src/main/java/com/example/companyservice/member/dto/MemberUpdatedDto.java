package com.example.companyservice.member.dto;

import com.example.companyservice.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberUpdatedDto {
    private Long memberId;

    private String name;

    private String phoneNumber;

    private String birthday;

    public static MemberUpdatedDto from(Member member) {
        return MemberUpdatedDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthday(member.getBirthday().toString())
                .build();
    }
}
