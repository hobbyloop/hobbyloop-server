package com.example.ticketservice.point.entity;

import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.ticket.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CompanyPointPolicy extends TimeStamped {
    @Id
    @Column(name = "company_point_policy")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private int usableScope;

    private int additionalPointPercentage;

    public boolean isGeneralScope() {
        return usableScope == PointUsableScopeEnum.GENERAL.getValue();
    }

    public boolean isSpecificCompanyScope() {
        return usableScope == PointUsableScopeEnum.SPECIFIC_COMPANY.getValue();
    }

    public boolean isSpecificCenterScope() {
        return usableScope == PointUsableScopeEnum.SPECIFIC_CENTER.getValue();
    }
}
