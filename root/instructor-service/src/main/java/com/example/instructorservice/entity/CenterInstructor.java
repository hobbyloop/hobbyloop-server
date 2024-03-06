package com.example.instructorservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterInstructor extends TimeStamped {

    @Id
    @Column(name = "center_instructor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int authority;

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private Long centerId;
}
