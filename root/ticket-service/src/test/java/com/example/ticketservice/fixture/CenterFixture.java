package com.example.ticketservice.fixture;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.HourResponseDto;

import java.time.LocalTime;
import java.util.List;

public class CenterFixture {

    public static final String DEFAULT_CENTER_NAME = "필라피티 스튜디오";
    public static final String DEFAULT_ADDRESS = "서울특별시 강남구 테헤란로 427";

    private static final LocalTime DEFAULT_OPEN_AT = LocalTime.parse("09:00");
    private static final LocalTime DEFAULT_CLOSE_AT = LocalTime.parse("18:00");

    private static final LocalTime DEFAULT_BREAK_OPEN_AT = LocalTime.parse("12:00");

    private static final LocalTime DEFAULT_BREAK_CLOSE_AT = LocalTime.parse("13:00");

    public static CenterInfoResponseDto defaultCenterInfoResponseDto() {
        return CenterInfoResponseDto.builder()
                .centerName(DEFAULT_CENTER_NAME)
                .address(DEFAULT_ADDRESS)
                .operatingHourList(defaultOperatingHourList)
                .breakHourList(defaultBreakingHourList)
                .build();
    }

    private static List<HourResponseDto> defaultOperatingHourList = List.of(
            HourResponseDto.builder().day("MON").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("TUE").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("WED").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("THU").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("FRI").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("SAT").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build(),
            HourResponseDto.builder().day("SUN").openAt(DEFAULT_OPEN_AT).closeAt(DEFAULT_CLOSE_AT).build()
    );

    private static List<HourResponseDto> defaultBreakingHourList = List.of(
            HourResponseDto.builder().day("MON").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("TUE").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("WED").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("THU").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("FRI").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("SAT").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build(),
            HourResponseDto.builder().day("SUN").openAt(DEFAULT_BREAK_OPEN_AT).closeAt(DEFAULT_BREAK_CLOSE_AT).build()
    );
}
