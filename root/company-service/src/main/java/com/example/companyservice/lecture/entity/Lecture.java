package com.example.companyservice.lecture.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.instructor.entity.InstructorCenter;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_group_id")
    private LectureGroup lectureGroup;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_center_id")
    private InstructorCenter instructorCenter;

    private String lectureImageKey;

    private String lectureImageUrl;

    private String desc;

    private int level;

    private int maxStudentCount;

    private int minStudentCount;
}
