package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "관리자 시설 리뷰 이용권 조회 응답 바디")
public class AdminReviewTicketResponseDto {

    @Schema(description = "시설 이름", example = "필라피티 스튜디오")
    private String centerName;

    @Schema(description = "시설 주소", example = "서울시 동작구 동작로 12-1")
    private String address;

    @Schema(description = "이용권 이름", example = "6:1 필라테스 15회")
    private String ticketName;

    @Schema(description = "리뷰 평균 점수", example = "4.5")
    private float score;

    public static AdminReviewTicketResponseDto of(CenterInfoResponseDto responseDto,
                                                  Ticket ticket,
                                                  float score) {
        return AdminReviewTicketResponseDto.builder()
                .centerName(responseDto.getCenterName())
                .address(responseDto.getAddress())
                .ticketName(ticket.getName())
                .score(score)
                .build();
    }
}
