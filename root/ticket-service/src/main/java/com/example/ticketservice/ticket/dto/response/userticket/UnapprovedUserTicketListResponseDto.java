package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.entity.UserTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnapprovedUserTicketListResponseDto {
    @Schema(description = "사용자 이용권 아이디", example = "1")
    private Long userTicketId;

    @Schema(description = "이용권 이름", example = "2:1 필라테스 20회")
    private String ticketName;

    @Schema(description = "본명", example = "이채림")
    private String memberName;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "이용권 구매 시점")
    private LocalDateTime createdAt;

    public static UnapprovedUserTicketListResponseDto of(UserTicket userTicket, MemberInfoResponseDto memberInfo) {
        return UnapprovedUserTicketListResponseDto.builder()
                .userTicketId(userTicket.getId())
                .ticketName(userTicket.getTicket().getName())
                .memberName(memberInfo.getMemberName())
                .phoneNumber(memberInfo.getPhoneNumber())
                .createdAt(userTicket.getCreatedAt())
                .build();
    }
}
