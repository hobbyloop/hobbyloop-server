package com.example.ticketservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TicketCreateRequestDto {

    private String name;

    private String category;

    private String introduce;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate expirationStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate expirationEndDate;

    private int duration;

    private int useCount;

    private boolean isTotalCount;

    private int totalCount;

    private int price;

    private int vat;

    private int discountRate;

    private int calculatedPrice;

    private int refundRegulation;

    private int refundPercentage;
}
