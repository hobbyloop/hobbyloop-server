package com.example.companyservice.lecture.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.dto.request.HourRequestDto;
import com.example.companyservice.company.dto.response.HourResponseDto;
import com.example.companyservice.company.entity.DayEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class LectureOperatingHour extends TimeStamped {
    @Id
    @Column(name = "lecture_operating_hour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`day`")
    private int day;

    private LocalTime openAt;

    private LocalTime closeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lecture lecture;

    public static LectureOperatingHour of(HourRequestDto dto, Lecture lecture) {
        return LectureOperatingHour.builder()
                .day(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .lecture(lecture)
                .build();
    }

    public static LectureOperatingHour of(HourResponseDto dto, Lecture lecture) {
        return LectureOperatingHour.builder()
                .day(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .lecture(lecture)
                .build();
    }
}
