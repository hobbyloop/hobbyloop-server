package com.example.companyservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterImage extends TimeStamped {

    @Id
    @Column(name = "center_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String centerImageKey;

    private String centerImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static CenterImage of(String centerImageKey, String centerImageUrl, Center center) {
        return CenterImage.builder()
                .centerImageKey(centerImageKey)
                .centerImageUrl(centerImageUrl)
                .center(center)
                .build();
    }
}
