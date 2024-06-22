package com.example.ticketservice.ticket.dto.response.userticket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "예약 가능 이용권 응답 바디")
public class AvailableUserTicketsWithCenterInfo {
    @Schema(description = "시설 아이디", example = "1")
    private Long centerId;

    @Schema(description = "시설 이름", example = "하비루프 스튜디오")
    private String centerName;

    @Schema(description = "중도환불가능업체 여부", example = "false")
    private boolean refundable;

    @Schema(description = "예약 가능 이용권 목록")
    private List<AvailableUserTicketResponseDto> availableUserTickets;

    public static AvailableUserTicketsWithCenterInfo of(Long centerId, String centerName, boolean isRefundable, List<AvailableUserTicketResponseDto> availableUserTickets) {
        return AvailableUserTicketsWithCenterInfo.builder()
                .centerId(centerId)
                .centerName(centerName)
                .refundable(isRefundable)
                .availableUserTickets(availableUserTickets)
                .build();
    }
}
