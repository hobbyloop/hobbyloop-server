package com.example.companyservice.company.entity;

import com.example.companyservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdvertisementLog extends TimeStamped {

    @Id
    @Column(name = "advertisement_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    private String logType;

    public static AdvertisementLog of(Advertisement advertisement, String logType) {
        return AdvertisementLog.builder()
                .advertisement(advertisement)
                .logType(logType)
                .build();
    }
}
