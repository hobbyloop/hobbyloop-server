package com.example.companyservice.company.entity;

import com.example.companyservice.common.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class QuickButton extends TimeStamped {

    @Id
    @Column(name = "quick_button_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int buttonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    public static QuickButton of(int buttonId, Center center) {
        return QuickButton.builder()
                .buttonId(buttonId)
                .center(center)
                .build();
    }
}
