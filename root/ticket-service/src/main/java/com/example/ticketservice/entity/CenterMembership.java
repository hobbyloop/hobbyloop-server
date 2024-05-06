package com.example.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CenterMembership extends TimeStamped {
    @Id
    @Column(name = "center_membership_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long centerId;

    private Long memberId;

    private String memberName;

    private String phoneNumber;

    @Setter
    private int status;

    public void updateMemberInfo(String memberName, String phoneNumber) {
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
    }

    public boolean isExpired() {
        return status == CenterMembershipStatusEnum.EXPIRED.getStatusType();
    }

    public boolean isExpiringSoon() {
        return status == CenterMembershipStatusEnum.EXPIRING_SOON.getStatusType();
    }

    public void renew() {
        status = CenterMembershipStatusEnum.RENEWED.getStatusType();
    }
}
