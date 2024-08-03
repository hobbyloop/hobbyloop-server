package com.example.companyservice.instructor.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.entity.Center;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class InstructorCenter extends TimeStamped {

    @Id
    @Column(name = "instructor_center_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    private int auth;

    public static InstructorCenter of(Instructor instructor, Center center) {
        return InstructorCenter.builder()
                .instructor(instructor)
                .center(center)
                .auth(InstructorAuthEnum.FULLTIME.getValue())
                .build();
    }
}
