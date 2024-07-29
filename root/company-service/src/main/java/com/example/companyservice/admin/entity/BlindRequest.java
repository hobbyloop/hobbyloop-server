package com.example.companyservice.admin.entity;

import com.example.companyservice.common.entity.TimeStamped;
import com.example.companyservice.company.entity.Center;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BlindRequest extends TimeStamped {

    @Id
    @Column(name = "blind_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    private Long reviewId;

    private String reason;

    private String status;

    public static BlindRequest of(Center center,
                                  Long reviewId,
                                  String reason,
                                  String status) {
        return BlindRequest.builder()
                .center(center)
                .reviewId(reviewId)
                .reason(reason)
                .status(status)
                .build();
    }
}
