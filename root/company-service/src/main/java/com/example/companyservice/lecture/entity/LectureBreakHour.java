package com.example.companyservice.lecture.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.dto.request.HourRequestDto;
import com.example.companyservice.company.dto.response.HourResponseDto;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.entity.CenterBreakHour;
import com.example.companyservice.company.entity.DayEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class LectureBreakHour extends TimeStamped {
    @Id
    @Column(name = "lecture_break_hour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`day`")
    private int day;

    private LocalTime openAt;

    private LocalTime closeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lecture lecture;

    public static LectureBreakHour of(HourRequestDto dto, Lecture lecture) {
        return LectureBreakHour.builder()
                .day(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .lecture(lecture)
                .build();
    }

    public static LectureBreakHour of(HourResponseDto dto, Lecture lecture) {
        return LectureBreakHour.builder()
                .day(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .lecture(lecture)
                .build();
    }
}
