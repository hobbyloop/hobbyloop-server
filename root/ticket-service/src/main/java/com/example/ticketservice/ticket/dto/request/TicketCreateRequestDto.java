package com.example.ticketservice.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(title = "이용권 등록 요청 바디")
public class TicketCreateRequestDto {

    @Schema(description = "이용권 이름", example = "6:1 필라테스 15회 이용권", required = true)
    private String name;

    @Schema(description = "카테고리", example = "필라테스")
    private String category;

    @Schema(description = "소개", example = "이용권입니다.")
    private String introduce;

    @Schema(description = "이용권 신청 마감기한 - 시작일", example = "2024-06-20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate expirationStartDate;

    @Schema(description = "이용권 신청 마감기한 - 종료일", example = "2024-06-20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate expirationEndDate;

    @Schema(description = "이용권 사용기한(일?)", example = "30")
    private int duration;

    @Schema(description = "이용권 사용가능 횟수(회원이 이용권 구매 후 사용할 수 있는 횟수)", example = "30")
    private int useCount;

    @Schema(description = "이용권 총 수량 설정 여부", example = "true")
    @JsonProperty("isTotalCount")
    private boolean isTotalCount;

    @Schema(description = "이용권 총 수량(최대 판매가능 수량)", example = "100")
    private int totalCount;

    @Schema(description = "가격", example = "150000")
    private int price;

    @Schema(description = "부가세", example = "15000")
    private int vat;

    @Schema(description = "할인율", example = "20")
    private int discountRate;

    @Schema(description = "이용권 총 가격(가격에서 할인율과 부가세 적용한 후 가격)", example = "130000")
    private int calculatedPrice;

    @Schema(description = "환불 규정")
    private int refundRegulation;

    @Schema(description = "환불 퍼센트(환불 규정이 5일 경우 필수)", example = "50")
    private int refundPercentage;
}
