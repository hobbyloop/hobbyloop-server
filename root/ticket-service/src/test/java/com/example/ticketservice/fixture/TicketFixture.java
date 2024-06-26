package com.example.ticketservice.fixture;

import com.example.ticketservice.ticket.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.ticket.entity.Ticket;

import java.time.LocalDate;

public class TicketFixture {

    public static final LocalDate EXPIRATION_START_DATE = LocalDate.of(2024, 1, 1);
    public static final LocalDate EXPIRATION_END_DATE = LocalDate.of(2024, 12, 31);

    public static final int USE_COUNT = 10;
    public static final int TOTAL_COUNT = 15;

    public static final int LOW_PRICE = 100000;
    public static final int MEDIUM_PRICE = 150000;
    public static final int HIGH_PRICE = 200000;

    public static TicketCreateRequestDto defaultTicketCreateRequest() {
        return TicketCreateRequestDto.builder()
                .name("6:1 필라테스 15회")
                .category("필라테스")
                .introduce("티켓 소개")
                .expirationStartDate(EXPIRATION_START_DATE)
                .expirationEndDate(EXPIRATION_END_DATE)
                .duration(60)
                .useCount(USE_COUNT)
                .isTotalCount(true)
                .totalCount(TOTAL_COUNT)
                .price(LOW_PRICE)
                .vat(1000)
                .discountRate(10)
                .calculatedPrice((int) (LOW_PRICE - (LOW_PRICE * 0.1)))
                .refundRegulation(1)
                .refundPercentage(10)
                .build();
    }

    public static Ticket uploadedTicket(Long centerId) {
        return Ticket.builder()
                .name("6:1 필라테스 15회")
                .category(1)
                .introduce("필라테스입니다.")
                .expirationStartDate(EXPIRATION_START_DATE)
                .expirationEndDate(EXPIRATION_END_DATE)
                .duration(30)
                .useCount(USE_COUNT)
                .isTotalCount(true)
                .totalCount(TOTAL_COUNT)
                .price(LOW_PRICE)
                .vat(1000)
                .discountRate(10)
                .calculatedPrice((int) (LOW_PRICE - (LOW_PRICE * 0.1)))
                .refundRegulation(1)
                .refundPercentage(10)
                .isUpload(true)
                .centerId(centerId)
                .build();
    }

    public static TicketCreateRequestDto mediumPriceTicketCreateRequest() {
        return TicketCreateRequestDto.builder()
                .name("티켓 이름")
                .category("필라테스")
                .introduce("티켓 소개")
                .expirationStartDate(EXPIRATION_START_DATE)
                .expirationEndDate(EXPIRATION_END_DATE)
                .duration(30)
                .useCount(USE_COUNT)
                .isTotalCount(true)
                .totalCount(TOTAL_COUNT)
                .price(MEDIUM_PRICE)
                .vat(1000)
                .discountRate(10)
                .calculatedPrice((int) (MEDIUM_PRICE - (MEDIUM_PRICE * 0.1)))
                .refundRegulation(1)
                .refundPercentage(10)
                .build();
    }

    public static TicketCreateRequestDto highPriceTicketCreateRequest() {
        return TicketCreateRequestDto.builder()
                .name("티켓 이름")
                .category("필라테스")
                .introduce("티켓 소개")
                .expirationStartDate(EXPIRATION_START_DATE)
                .expirationEndDate(EXPIRATION_END_DATE)
                .duration(30)
                .useCount(USE_COUNT)
                .isTotalCount(true)
                .totalCount(TOTAL_COUNT)
                .price(HIGH_PRICE)
                .vat(1000)
                .discountRate(10)
                .calculatedPrice((int) (HIGH_PRICE - (HIGH_PRICE * 0.1)))
                .refundRegulation(1)
                .refundPercentage(10)
                .build();
    }
}
