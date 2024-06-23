package com.example.ticketservice.ticket.dto.response.centermembership;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketResponseDto;
import com.example.ticketservice.ticket.entity.CenterMembership;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "시설 회원 상세 응답 바디")
public class CenterMembershipDetailResponseDto {
    @Schema(description = "시설 회원 아이디", example = "1")
    private long centerMembershipId;

    @Schema(description = "회원 본명", example = "이채림")
    private String memberName;

    @Schema(description = "회원 생일", example = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "회원 성별", example = "남 or 여")
    private String gender;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "시설 회원 소유 이용권 목록")
    private List<TicketResponseDto> tickets;

    public static CenterMembershipDetailResponseDto of(CenterMembership centerMembership, List<TicketResponseDto> tickets) {
        return CenterMembershipDetailResponseDto.builder()
                .centerMembershipId(centerMembership.getId())
                .memberName(centerMembership.getMemberName())
                .birthday(centerMembership.getBirthday())
                .gender(centerMembership.getGender())
                .phoneNumber(centerMembership.getPhoneNumber())
                .tickets(tickets)
                .build();
    }
}
