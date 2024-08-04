package com.example.companyservice.lecture.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.entity.Center;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class LectureGroup extends TimeStamped {

    @Id
    @Column(name = "lecture_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    private LocalDate repetitionStartDate;

    private LocalDate repetitionEndDate;


}
