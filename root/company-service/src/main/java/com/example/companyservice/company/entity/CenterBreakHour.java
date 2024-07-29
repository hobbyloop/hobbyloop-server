package com.example.companyservice.company.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.dto.request.HourRequestDto;
import com.example.companyservice.company.dto.response.HourResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterBreakHour extends TimeStamped {

    @Id
    @Column(name = "center_break_hour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`day`")
    private int day;

    private LocalTime openAt;

    private LocalTime closeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static CenterBreakHour of(HourRequestDto dto, Center center) {
        return CenterBreakHour.builder()
                .day(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .center(center)
                .build();
    }

    public static CenterBreakHour of(HourResponseDto dto, Center center) {
        return CenterBreakHour.builder()
                .day(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .center(center)
                .build();
    }
}
