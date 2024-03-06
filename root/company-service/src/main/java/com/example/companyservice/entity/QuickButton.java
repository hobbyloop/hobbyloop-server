package com.example.companyservice.entity;

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

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;
}
