package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.entity.UserTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableUserTicketResponseDto {
    @Schema(description = "사용자 이용권 아이디", example = "1")
    private long userTicketId;

    @Schema(description = "이용권 이름", example = "2:1 필라테스 30회")
    private String ticketName;

    @Schema(description = "이용권 이미지 URL")
    private String ticketImageUrl;

    @Schema(description = "사용자 이용권 사용가능기한 - 시작일", example = "2024-06-20")
    private LocalDate startDate;

    @Schema(description = "사용자 이용권 사용가능기한 - 종료일", example = "2024-06-22")
    private LocalDate endDate;

    public static AvailableUserTicketResponseDto of(UserTicket userTicket) {
        return AvailableUserTicketResponseDto.builder()
                .userTicketId(userTicket.getId())
                .ticketName(userTicket.getTicket().getName())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .startDate(userTicket.getStartDate())
                .endDate(userTicket.getEndDate())
                .build();
    }
}
