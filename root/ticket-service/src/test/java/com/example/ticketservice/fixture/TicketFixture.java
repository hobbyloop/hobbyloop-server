package com.example.ticketservice.fixture;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

public class TicketFixture {

    public static final LocalDate EXPIRATION_START_DATE = LocalDate.of(2024, 1, 1);
    public static final LocalDate EXPIRATION_END_DATE = LocalDate.of(2024, 12, 31);

    public static final int USE_COUNT = 10;
    public static final int TOTAL_COUNT = 15;

    public static TicketCreateRequestDto defaultTicketCreateRequest() throws UnsupportedEncodingException {
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
                .price(10000)
                .vat(1000)
                .discountRate(10)
                .calculatedPrice(9000)
                .refundRegulation(1)
                .refundPercentage(10)
                .build();
    }
}
