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
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "이용권 소멸 내역 응답 바디")
public class UserTicketExpiringHistoryResponseDto {
    @Schema(description = "이용권 아이디", example = "1")
    private Long ticketId;

    @Schema(description = "이용권 이미지 URL")
    private String ticketImageUrl;

    @Schema(description = "이용권 이름", example = "2:1 필라테스 30회")
    private String ticketName;

    @Schema(description = "시설 이름", example = "필라피티 스튜디오")
    private String centerName;

    @Schema(description = "남은 사용가능 횟수", example = "7")
    private int remainingCount;

    @Schema(description = "총 횟수", example = "30")
    private int totalCounting;

    @Schema(description = "이용권이 소멸된 연월(이 필드 기준으로 내림차순 정렬)", example = "2024/06")
    private String yearMonth;

    @Schema(description = "소멸 횟수", example = "1")
    private int expireCount;

    @Schema(description = "소멸 시점(이 필드 기준으로 내림차순 정렬)", example = "2024-06-01T15:59:18.364433")
    private LocalDate expiredAt;

    public static UserTicketExpiringHistoryResponseDto of(UserTicket userTicket, String centerName, String yearMonth) {
        return UserTicketExpiringHistoryResponseDto.builder()
                .ticketId(userTicket.getId())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .ticketName(userTicket.getTicket().getName())
                .centerName(centerName)
                .remainingCount(0)
                .totalCounting(userTicket.getTicket().getUseCount())
                .yearMonth(yearMonth)
                .expireCount(userTicket.getRemainingCount())
                .expiredAt(userTicket.getEndDate())
                .build();
    }
}
