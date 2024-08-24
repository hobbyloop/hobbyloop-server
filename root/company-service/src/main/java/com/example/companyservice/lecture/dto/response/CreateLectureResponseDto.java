package com.example.companyservice.lecture.dto.response;

import com.example.companyservice.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLectureResponseDto {
    private Long lectureId;
    private String ticketName;
    private String lectureName;
    private String centerName;
    private String lectureImageUrl;
    private LocalDate openingDate;
    private LocalDateTime reservationStartDateTime;
    private LocalDateTime reservationCancellableDateTime;
    private String instructorName;
    private int level;
    private int maxStudentCount;
    private int minStudentCount;

    public static CreateLectureResponseDto of(Lecture lecture, String ticketName, String centerName) {
        return CreateLectureResponseDto.builder()
                .lectureId(lecture.getId())
                .ticketName(ticketName)
                .lectureName(lecture.getName())
                .centerName(centerName)
                .lectureImageUrl(lecture.getLectureImageUrl())
                .openingDate(lecture.getOpeningDate())
                .reservationStartDateTime(lecture.getReservationStartDateTime())
                .reservationCancellableDateTime(lecture.getReservationCancellableDateTime())
                .instructorName(lecture.getInstructorCenter().getInstructor().getName())
                .level(lecture.getLevel())
                .maxStudentCount(lecture.getMaxStudentCount())
                .minStudentCount(lecture.getMinStudentCount())
                .build();
    }
}
