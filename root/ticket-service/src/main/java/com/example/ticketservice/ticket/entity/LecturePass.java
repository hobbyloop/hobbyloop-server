package com.example.ticketservice.ticket.entity;

import com.example.ticketservice.common.entity.TimeStamped;
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
