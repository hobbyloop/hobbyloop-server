package com.example.companyservice.fixture;

import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;

import java.time.LocalDate;

public class MemberFixture {
    public static final String NAME = "김하비";
    public static final String UPDATED_NAME = "김길동";

    public static CreateMemberRequestDto defaultCreateMemberRequest() {
        return CreateMemberRequestDto.builder()
                .name(NAME)
                .email("hobbyloop@gmail.com")
                .nickname("하비")
                .gender(1)
                .birthday(LocalDate.of(2000, 1, 1))
                .isOption1(true)
                .isOption2(true)
                .provider("apple")
                .subject("이게뭐지")
                .oauth2AccessToken("토큰")
                .ci("이게뭐지")
                .di("이게뭐야")
                .build();
    }

    public static MemberUpdateRequestDto defaultMemberUpdateRequest() {
        return new MemberUpdateRequestDto(UPDATED_NAME, "길동", LocalDate.of(2000, 2, 2));
    }
}
