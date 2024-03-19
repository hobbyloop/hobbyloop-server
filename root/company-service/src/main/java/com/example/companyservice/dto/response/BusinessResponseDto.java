package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessResponseDto {

    private String representativeName;

    private LocalDate openingDate;

    private String businessNumber;

    private String onlineReportNumber;

    public static BusinessResponseDto from(Center center) {
        return BusinessResponseDto.builder()
                .representativeName(center.getRepresentativeName())
                .openingDate(center.getOpeningDate())
                .businessNumber(center.getBusinessNumber())
                .onlineReportNumber(center.getOnlineReportNumber())
                .build();
    }
}
