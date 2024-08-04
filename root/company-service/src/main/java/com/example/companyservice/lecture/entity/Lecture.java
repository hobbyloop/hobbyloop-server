package com.example.companyservice.lecture.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.instructor.entity.InstructorCenter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Lecture extends TimeStamped {

    @Id
    @Column(name = "lecture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_center_id")
    private InstructorCenter instructorCenter;

    private Long ticketId;

    private Boolean isRepeatable;

    private String lectureImageKey;

    private String lectureImageUrl;

    private String desc;

    private Integer level;

    private Integer maxStudentCount;

    private Integer minStudentCount;

    private LocalDateTime reservationStartDateTime;

    private LocalDateTime reservationEndDateTime;

    private LocalDate openingDate;

    private LocalDateTime reservationCancellableDateTime;

    private LocalDate repetitionStartDate;

    private LocalDate repetitionEndDate;
}
