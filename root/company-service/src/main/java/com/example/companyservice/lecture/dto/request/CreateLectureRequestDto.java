package com.example.companyservice.lecture.dto.request;

import com.example.companyservice.company.dto.request.HourRequestDto;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.instructor.entity.InstructorCenter;
import com.example.companyservice.lecture.entity.Lecture;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateLectureRequestDto {
    private String name;

    private Long instructorCenterId;
    private Long ticketId;

    private String ticketName;
    private Boolean isRepeatable;
    private String desc;
    private Integer level;
    private Integer maxStudentCount;
    private Integer minStudentCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reservationStartDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reservationEndDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate openingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reservationCancellableDateTime;

    private List<HourRequestDto> lectureOperatingHours;

    private List<HourRequestDto> lectureBreakHours;

    public Lecture toEntity(Center center, InstructorCenter instructorCenter, Long ticketId, String lectureImageKey, String lectureImageUrl) {
        return Lecture.builder()
                .name(name)
                .center(center)
                .instructorCenter(instructorCenter)
                .ticketId(ticketId)
                .isRepeatable(isRepeatable)
                .desc(desc)
                .level(level)
                .maxStudentCount(maxStudentCount)
                .minStudentCount(minStudentCount)
                .lectureImageKey(lectureImageKey)
                .lectureImageUrl(lectureImageUrl)
                .reservationStartDateTime(reservationStartDateTime)
                .reservationEndDateTime(reservationEndDateTime)
                .openingDate(openingDate)
                .reservationCancellableDateTime(reservationCancellableDateTime)
                .build();
    }
}
