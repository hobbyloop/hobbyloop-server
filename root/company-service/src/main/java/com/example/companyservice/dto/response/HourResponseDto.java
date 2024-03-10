package com.example.companyservice.dto.response;

import com.example.companyservice.entity.DayEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HourResponseDto {

    private String day;

    private LocalTime openAt;

    private LocalTime closeAt;

    public static HourResponseDto of(int day, LocalTime openAt, LocalTime closeAt) {
        return HourResponseDto.builder()
                .day(DayEnum.findByValue(day).getDayName())
                .openAt(openAt)
                .closeAt(closeAt)
                .build();
    }
}
