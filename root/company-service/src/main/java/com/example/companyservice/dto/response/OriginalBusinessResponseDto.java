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
public class OriginalBusinessResponseDto {

    private String representativeName;

    private String businessNumber;

    private LocalDate openingDate;

    private String onlineReportNumber;

    public static OriginalBusinessResponseDto from(Center center) {
        return OriginalBusinessResponseDto.builder()
                .representativeName(center.getRepresentativeName())
                .businessNumber(center.getBusinessNumber())
                .openingDate(center.getOpeningDate())
                .onlineReportNumber(center.getOnlineReportNumber())
                .build();
    }
}
