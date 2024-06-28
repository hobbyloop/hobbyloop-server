package com.example.companyservice.company.client.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "이용권 정보")
public class TicketClientResponseDto {

    @Schema(description = "이용권 아이디", example = "1")
    private Long ticketId;

    @Schema(description = "이용권 이름", example = "6:1 필라테스 15회")
    private String name;

    @Schema(description = "이용권 총 가격(가격에서 할인율과 부가세 적용한 후 가격)", example = "130000")
    private int calculatedPrice;

    @Schema(description = "이용권 사용기한(일?)", example = "30")
    private int duration;
}
