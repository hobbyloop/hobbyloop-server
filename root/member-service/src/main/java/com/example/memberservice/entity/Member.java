package com.example.memberservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Member extends TimeStamped {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String nickname;

    private int gender;

    private LocalDate birthday;

    private String phoneNumber;

    private int point;

    private boolean isOption1;

    private boolean isOption2;

    private boolean isDelete;
}
