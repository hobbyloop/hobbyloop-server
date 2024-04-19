package com.example.companyservice.fixture;

import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import com.example.companyservice.company.dto.request.HourRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CenterFixture {
    public static final String DEFAULT_CENTER_NAME = "필라피티 스튜디오";
    public static final String DEFAULT_ADDRESS = "서울특별시 중구 427";

    public static final double LATITUDE = 37.56100278;
    public static final double LONGITUDE = 126.9996417;

    private static final LocalTime DEFAULT_OPEN_AT = LocalTime.parse("09:00");
    private static final LocalTime DEFAULT_CLOSE_AT = LocalTime.parse("18:00");

    private static final LocalTime DEFAULT_BREAK_OPEN_AT = LocalTime.parse("12:00");

    private static final LocalTime DEFAULT_BREAK_CLOSE_AT = LocalTime.parse("13:00");

    public static CenterCreateRequestDto defaultCenterCreateRequest() {
        return CenterCreateRequestDto.builder()
                .centerName(DEFAULT_CENTER_NAME)
                .address(DEFAULT_ADDRESS)
                .announcement("test-announcement")
                .introduce("test-introduce")
                .contact("test-contact")
                .kakaoLink("test-kakao-link")
                .operatingHourList(defaultOperatingHourList)
                .breakHourList(defaultBreakingHourList)
                .representativeName("홍길동")
                .businessNumber("123-45-67890")
                .openingDate(LocalDate.of(2021, 1, 1))
                .onlineReportNumber("1234567890")
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .build();
    }

    private static List<HourRequestDto> defaultOperatingHourList = List.of(
            new HourRequestDto("월", DEFAULT_OPEN_AT, DEFAULT_CLOSE_AT),
            new HourRequestDto("화", DEFAULT_OPEN_AT, DEFAULT_CLOSE_AT),
            new HourRequestDto("수", DEFAULT_OPEN_AT, DEFAULT_CLOSE_AT),
            new HourRequestDto("목", DEFAULT_OPEN_AT, DEFAULT_CLOSE_AT),
            new HourRequestDto("금", DEFAULT_OPEN_AT, DEFAULT_CLOSE_AT),
            new HourRequestDto("토", DEFAULT_OPEN_AT, DEFAULT_CLOSE_AT)
    );

    private static List<HourRequestDto> defaultBreakingHourList = List.of(
            new HourRequestDto("월", DEFAULT_BREAK_OPEN_AT, DEFAULT_BREAK_CLOSE_AT),
            new HourRequestDto("화", DEFAULT_BREAK_OPEN_AT, DEFAULT_BREAK_CLOSE_AT),
            new HourRequestDto("수", DEFAULT_BREAK_OPEN_AT, DEFAULT_BREAK_CLOSE_AT),
            new HourRequestDto("목", DEFAULT_BREAK_OPEN_AT, DEFAULT_BREAK_CLOSE_AT),
            new HourRequestDto("금", DEFAULT_BREAK_OPEN_AT, DEFAULT_BREAK_CLOSE_AT),
            new HourRequestDto("토", DEFAULT_BREAK_OPEN_AT, DEFAULT_BREAK_CLOSE_AT)
    );
}
