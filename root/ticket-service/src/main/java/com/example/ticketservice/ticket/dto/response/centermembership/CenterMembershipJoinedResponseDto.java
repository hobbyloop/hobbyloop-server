package com.example.ticketservice.ticket.dto.response.centermembership;

import com.example.ticketservice.ticket.dto.request.CenterMembershipJoinRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "시설 회원 목록 응답 바디")
public class CenterMembershipJoinedResponseDto {
    @Schema(description = "회원 본명", example = "이채림")
    private String memberName;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "회원 성별", example = "남 or 여")
    private String gender;

    @Schema(description = "회원 이메일", example = "hobbyloop@gmail.com")
    private String email;

    @Schema(description = "회원 생일", example = "yyyy-MM-dd")
    private String birthday;

    @Schema(description = "회원이 구매한 이용권 이름", example = "6:1 필라테스 15회 이용권")
    private String ticketName;

    public static CenterMembershipJoinedResponseDto of(CenterMembershipJoinRequestDto request, String ticketName) {
        return CenterMembershipJoinedResponseDto.builder()
                .memberName(request.getMemberName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .email(request.getEmail())
                .birthday(request.getBirthday())
                .ticketName(ticketName)
                .build();
    }
}
