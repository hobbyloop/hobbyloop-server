package com.example.ticketservice.fixture;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.HourResponseDto;
import com.example.ticketservice.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.client.dto.response.OriginalCenterResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CenterFixture {

    public static final String DEFAULT_CENTER_NAME = "필라피티 스튜디오";
    public static final String DEFAULT_ADDRESS = "서울특별시 강남구 테헤란로 427";

    public static final String NON_REFUNDABLE_CENTER_NAME = "모어겐 스튜디오";

    private static final LocalTime DEFAULT_OPEN_AT = LocalTime.parse("09:00");
    private static final LocalTime DEFAULT_CLOSE_AT = LocalTime.parse("18:00");

    private static final LocalTime DEFAULT_BREAK_OPEN_AT = LocalTime.parse("12:00");

    private static final LocalTime DEFAULT_BREAK_CLOSE_AT = LocalTime.parse("13:00");

    public static CenterInfoResponseDto defaultCenterInfoResponseDto() {
        return CenterInfoResponseDto.builder()
                .centerName(DEFAULT_CENTER_NAME)
                .address(DEFAULT_ADDRESS)
                .announcement("test-announcement")
                .introduce("test-introduce")
                .contact("test-contact")
                .kakaoLink("test-kakao-link")
                .looppass(true)
                .refundable(true)
                .operatingHourList(defaultOperatingHourList)
                .breakHourList(defaultBreakingHourList)
                .build();
    }

    public static CenterInfoResponseDto nonRefundableCenterInfoResponseDto() {
        return CenterInfoResponseDto.builder()
                .centerName(NON_REFUNDABLE_CENTER_NAME)
                .address(DEFAULT_ADDRESS)
                .announcement("test-announcement")
                .introduce("test-introduce")
                .contact("test-contact")
                .kakaoLink("test-kakao-link")
                .looppass(true)
                .refundable(false)
                .operatingHourList(defaultOperatingHourList)
                .breakHourList(defaultBreakingHourList)
                .build();
    }

    public static OriginalCenterResponseDto defaultOriginalCenterResponseDto() {
        return OriginalCenterResponseDto.builder()
                .centerName(DEFAULT_CENTER_NAME)
                .address(DEFAULT_ADDRESS)
                .logoImageUrl("test-logo-image-url")
                .centerImageUrl(List.of("test-center-image-url"))
                .announcement("test-announcement")
                .introduce("test-introduce")
                .contact("test-contact")
                .kakaoLink("test-kakao-link")
                .operatingHourList(defaultOperatingHourList)
                .breakHourList(defaultBreakingHourList)
                .build();
    }

    public static OriginalBusinessResponseDto defaultOriginalBusinessResponseDto() {
        return OriginalBusinessResponseDto.builder()
                .representativeName("홍길동")
                .businessNumber("123-45-67890")
                .openingDate(LocalDate.of(2021, 1, 1))
                .onlineReportNumber("1234567890")
                .build();
    }

    private static List<HourResponseDto> defaultOperatingHourList = List.of(
            HourResponseDto.builder().day("월").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("화").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("수").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("목").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("금").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("토").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("일").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build()
    );

    private static List<HourResponseDto> defaultBreakingHourList = List.of(
            HourResponseDto.builder().day("월").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("화").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("수").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("목").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("금").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("토").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("일").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build()
    );
}
