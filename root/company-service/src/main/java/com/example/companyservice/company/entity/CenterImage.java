package com.example.companyservice.company.entity;

import com.example.companyservice.common.entity.TimeStamped;
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

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static CenterImage of(String centerImageKey, String centerImageUrl, Center center) {
        return CenterImage.builder()
                .centerImageKey(centerImageKey)
                .centerImageUrl(centerImageUrl)
                .isDeleted(false)
                .center(center)
                .build();
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
