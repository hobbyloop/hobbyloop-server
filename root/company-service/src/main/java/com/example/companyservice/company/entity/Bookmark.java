package com.example.companyservice.company.entity;

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

    private Long memberId;

    public static Bookmark of(Center center, Long memberId) {
        return Bookmark.builder()
                .center(center)
                .memberId(memberId)
                .build();
    }
}
