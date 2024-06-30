package com.example.companyservice.member.entity;

import com.example.companyservice.company.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MemberArchive extends TimeStamped {
    @Id
    @Column(name = "member_archive_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private String di;

    public static MemberArchive from(Member member) {
        return MemberArchive.builder()
                .phoneNumber(member.getPhoneNumber())
                .di(member.getDi())
                .build();
    }
}
