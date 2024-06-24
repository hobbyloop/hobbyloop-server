package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketByCenterResponseDto {
    @Schema(description = "이용권 아이디", example = "1")
    private Long ticketId;

    @Schema(description = "이용권 이름", example = "2:1 필라테스 20회")
    private String ticketName;

    @Schema(description = "가격(할인 전)", example = "350000")
    private int price;

    @Schema(description = "할인률", example = "10")
    private int discountRate;

    @Schema(description = "최종 가격(할인 후)", example = "315000")
    private int calculatedPrice;

    public static TicketByCenterResponseDto from(Ticket ticket) {
        return TicketByCenterResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketName(ticket.getName())
                .price(ticket.getPrice())
                .discountRate(ticket.getDiscountRate())
                .calculatedPrice(ticket.getCalculatedPrice())
                .build();
    }
}
