package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.ticket.entity.CategoryEnum;
import com.example.ticketservice.ticket.entity.Ticket;
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
@Schema(title = "이용권 상세 응답 바디")
public class TicketDetailResponseDto {
    @Schema(description = "이용권 이름", example = "6:1 필라테스 15회")
    private String ticketName;

    @Schema(description = "이용권 이미지 URL")
    private String ticketImageUrl;

    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "소개", example = "이용권입니다.")
    private String introduce;

    @Schema(description = "이용권 신청 마감기한 - 시작일", example = "2024-06-20")
    private LocalDate expirationStartDate;

    @Schema(description = "이용권 신청 마감기한 - 종료일", example = "2024-06-20")
    private LocalDate expirationEndDate;

    @Schema(description = "이용권 사용기한(일?)", example = "30")
    private int duration;

    @Schema(description = "이용권 사용가능 횟수(회원이 이용권 구매 후 사용할 수 있는 횟수)", example = "30")
    private int useCount;

    @Schema(description = "이용권 총 수량 설정 여부", example = "true")
    private boolean isTotalCount;

    @Schema(description = "이용권 총 수량(최대 판매가능 수량)", example = "100")
    private int totalCount;

    @Schema(description = "이용권 발급 횟수(사용자가 구매한 횟수)", example = "16")
    private int issueCount;

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

    @Schema(description = "시설 정보")
    private CenterInfoResponseDto centerInfo;

    @Schema(description = "뭐지")
    private OriginalBusinessResponseDto businessInfo;

    public static TicketDetailResponseDto of(Ticket ticket, CenterInfoResponseDto centerInfo, OriginalBusinessResponseDto businessInfo) {
        return TicketDetailResponseDto.builder()
                .ticketName(ticket.getName())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .category(CategoryEnum.findByValue(ticket.getCategory()).getName())
                .introduce(ticket.getIntroduce())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .duration(ticket.getDuration())
                .useCount(ticket.getUseCount())
                .isTotalCount(ticket.isTotalCount())
                .totalCount(ticket.getTotalCount())
                .issueCount(ticket.getIssueCount())
                .price(ticket.getPrice())
                .vat(ticket.getVat())
                .discountRate(ticket.getDiscountRate())
                .calculatedPrice(ticket.getCalculatedPrice())
                .refundRegulation(ticket.getRefundRegulation())
                .refundPercentage(ticket.getRefundPercentage())
                .centerInfo(centerInfo)
                .businessInfo(businessInfo)
                .build();
    }
}
