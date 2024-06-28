package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.DayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "시설 시간표")
public class HourResponseDto {

    @Schema(description = "운영 시간 리스트", example = "월")
    private String day;

    @Schema(description = "시작 시간")
    private LocalTime openAt;

    @Schema(description = "시작 시간")
    private LocalTime closeAt;

    public static HourResponseDto of(int day, LocalTime openAt, LocalTime closeAt) {
        return HourResponseDto.builder()
                .day(DayEnum.findByValue(day).getDayName())
                .openAt(openAt)
                .closeAt(closeAt)
                .build();
    }
}
