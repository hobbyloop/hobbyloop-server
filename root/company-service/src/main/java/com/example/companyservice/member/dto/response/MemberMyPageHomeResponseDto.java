package com.example.companyservice.member.dto.response;

import com.example.companyservice.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMyPageHomeResponseDto {
    private String name;
    private String nickname;
    private String phoneNumber;
    private String profileImageUrl;
    private Long points;
    private Long ticketCount;
    private Long couponCount;

    public static MemberMyPageHomeResponseDto of(Member member, Long points, Long ticketCount, Long couponCount) {
        return MemberMyPageHomeResponseDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .points(points)
                .ticketCount(ticketCount)
                .couponCount(couponCount)
                .build();
    }
}
