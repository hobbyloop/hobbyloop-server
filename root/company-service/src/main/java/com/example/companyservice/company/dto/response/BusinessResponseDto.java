package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "시설 관지라 페이지 시설 상세 응답 바디")
public class BusinessResponseDto {

    @Schema(description = "대표자 이름")
    private String representativeName;

    @Schema(description = "개업 일자")
    private LocalDate openingDate;

    @Schema(description = "사업자 번호")
    private String businessNumber;

    @Schema(description = "통신판매번호")
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
