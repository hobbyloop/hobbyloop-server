package com.example.companyservice.member.dto.response;

import com.example.companyservice.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "본명", example = "이채림")
    private String name;

    @Schema(description = "닉네임", example = "하비")
    private String nickname;

    @Schema(description = "생년월일", example = "1999-01-01")
    private LocalDate birthday;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "프로필 사진 URL")
    private String profileImageUrl;

    public static MemberDetailResponseDto from(Member member) {
        return MemberDetailResponseDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .phoneNumber(member.getPhoneNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
