package com.example.ticketservice.point.entity;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Point extends TimeStamped {
    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long companyId;

    private Long centerId;

    private Long balance;

    private int usableScope;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (usableScope == PointUsableScopeEnum.SPECIFIC_COMPANY.getValue()) {
            if (companyId == null) {
                throw new ApiException(ExceptionEnum.INVALID_POINT_SCOPE_EXCEPTION);
            }
        } else if (usableScope == PointUsableScopeEnum.SPECIFIC_CENTER.getValue()) {
            if (centerId == null) {
                throw new ApiException(ExceptionEnum.INVALID_POINT_SCOPE_EXCEPTION);
            }
        } else if (usableScope == PointUsableScopeEnum.GENERAL.getValue()) {
            if (companyId != null || centerId != null) {
                throw new ApiException(ExceptionEnum.INVALID_POINT_SCOPE_EXCEPTION);
            }
        }
    }

    public void earn(Long amount) {
        balance += amount;
    }

    public void use(Long amount) {
        balance -= amount;
    }
}
