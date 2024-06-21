package com.example.ticketservice.ticket.dto.response.centermembership;

import com.example.ticketservice.ticket.entity.CenterMembership;
import com.example.ticketservice.ticket.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.ticket.entity.Ticket;
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
public class CenterMemberResponseDto {
    @Schema(description = "시설 회원 아이디", example = "1")
    private long centerMembershipId;

    @Schema(description = "회원 본명", example = "이채림")
    private String memberName;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "시설 회원 상태: 회원, 만료예정, 만료, 재가입", example = "회원")
    private String status;

    @Schema(description = "회원이 구매한 시설의 이용권 이름", example = "6:1 필라테스 15회 이용권")
    private String ticketName;

    public static CenterMemberResponseDto of(CenterMembership centerMembership, Ticket ticket) {
        return CenterMemberResponseDto.builder()
                .centerMembershipId(centerMembership.getId())
                .memberName(centerMembership.getMemberName())
                .phoneNumber(centerMembership.getPhoneNumber())
                .status(CenterMembershipStatusEnum.findByValue(centerMembership.getStatus()).getName())
                .ticketName(ticket.getName())
                .build();
    }
}
