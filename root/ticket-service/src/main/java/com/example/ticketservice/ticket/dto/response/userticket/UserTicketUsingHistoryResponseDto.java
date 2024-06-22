package com.example.ticketservice.ticket.dto.response.userticket;

import com.example.ticketservice.ticket.entity.UserTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "이용권 사용 내역 응답 바디", description = "사용 내역과 응답 구조가 다른데, 같은 이용권을 구매하더라도 기존 이용권에 충전되는 방식이 아니며, 하나의 이용권 당 반드시 만료 기한 시점에 한 번의 소멸만 일어날 수 있기 때문")
public class UserTicketUsingHistoryResponseDto {
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

    @Schema(description = "해당 이용권의 사용 내역(월별로 그룹핑)")
    private List<UsingHistoryByMonthDto> usingHistoryByMonth;

    public static UserTicketUsingHistoryResponseDto of(UserTicket userTicket, String centerName, List<UsingHistoryByMonthDto> usingHistoryByMonth) {
        return UserTicketUsingHistoryResponseDto.builder()
                .ticketId(userTicket.getTicket().getId())
                .ticketImageUrl(userTicket.getTicket().getTicketImageUrl())
                .ticketName(userTicket.getTicket().getName())
                .centerName(centerName)
                .remainingCount(userTicket.getRemainingCount())
                .totalCounting(userTicket.getTicket().getUseCount())
                .usingHistoryByMonth(usingHistoryByMonth)
                .build();
    }
}
