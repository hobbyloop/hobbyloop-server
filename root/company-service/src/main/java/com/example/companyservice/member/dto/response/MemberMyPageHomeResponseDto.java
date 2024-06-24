package com.example.companyservice.member.dto.response;

import com.example.companyservice.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMyPageHomeResponseDto {
    @Schema(description = "본명", example = "이채림")
    private String name;

    @Schema(description = "닉네임", example = "하비")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "프로필 사진 URL")
    private String profileImageUrl;

    @Schema(description = "보유 포인트 잔액", example = "50000")
    private Long points;

    @Schema(description = "보유 이용권 개수", example = "3")
    private Long ticketCount;

    @Schema(description = "보유 쿠폰 개수", example = "2")
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
