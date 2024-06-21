package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "이용권 생성 응답 바디")
public class TicketCreateResponseDto {
    @Schema(description = "이용권 아이디", example = "1")
    private long ticketId;

    @Schema(description = "이용권 이미지 URL")
    private String ticketImageUrl;

    @Schema(description = "시설 이름", example = "필라피티 스튜디오")
    private String centerName;

    @Schema(description = "이용권 이름", example = "6:1 필라테스 15회")
    private String ticketName;

    @Schema(description = "시설 주소", example = "서울시 동작구 동작로 12-1")
    private String address;

    @Schema(description = "이용권 총 수량(최대 판매가능 수량)", example = "100")
    private int totalCount;

    @Schema(description = "이용권 사용 가능 횟수", example = "15")
    private int useCount;

    @Schema(description = "이용권 신청 마감기한 - 시작일", example = "2024-06-20")
    private LocalDate expirationStartDate;

    @Schema(description = "이용권 신청 마감기한 - 종료일", example = "2024-06-20")
    private LocalDate expirationEndDate;

    public static TicketCreateResponseDto of(CenterInfoResponseDto centerInfo, Ticket ticket) {
        return TicketCreateResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .centerName(centerInfo.getCenterName())
                .address(centerInfo.getAddress())
                .ticketName(ticket.getName())
                .totalCount(ticket.getTotalCount())
                .useCount(ticket.getUseCount())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .build();
    }
}
