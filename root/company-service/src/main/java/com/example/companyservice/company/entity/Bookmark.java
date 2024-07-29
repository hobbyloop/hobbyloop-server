package com.example.companyservice.company.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Bookmark extends TimeStamped {

    @Id
    @Column(name = "bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Bookmark of(Center center, Member member) {
        return Bookmark.builder()
                .center(center)
                .member(member)
                .build();
    }
}
