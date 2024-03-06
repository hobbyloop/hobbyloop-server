package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class LecturePass extends TimeStamped {

    @Id
    @Column(name = "lecture_pass_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
