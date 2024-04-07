package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.client.dto.response.OriginalCenterResponseDto;
import com.example.ticketservice.entity.CategoryEnum;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailResponseDto {
    private String ticketName;
    private String ticketImageUrl;
    private String category;
    private String introduce;
    private LocalDate expirationStartDate;
    private LocalDate expirationEndDate;
    private int duration;
    private int useCount;
    private boolean isTotalCount;
    private int totalCount;
    private int issueCount;
    private int price;
    private int vat;
    private int discountRate;
    private int calculatedPrice;
    private int refundRegulation;
    private int refundPercentage;
    private OriginalCenterResponseDto centerInfo;
    private OriginalBusinessResponseDto businessInfo;

    public static TicketDetailResponseDto of(Ticket ticket, OriginalCenterResponseDto centerInfo, OriginalBusinessResponseDto businessInfo) {
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
