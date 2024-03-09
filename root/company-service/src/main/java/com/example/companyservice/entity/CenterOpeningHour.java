package com.example.companyservice.entity;

import com.example.companyservice.dto.request.OperatingHourListDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterOpeningHour extends TimeStamped {

    @Id
    @Column(name = "center_opening_hour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayName;

    private LocalTime openAt;

    private LocalTime closeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static CenterOpeningHour of(OperatingHourListDto dto, Center center) {
        return CenterOpeningHour.builder()
                .dayName(DayEnum.findByName(dto.getDay()).getTypeValue())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .center(center)
                .build();
    }
}
