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
@Schema(title = "수정 전 사업자 정보 응답 바디")
public class OriginalBusinessResponseDto {

    @Schema(description = "대표자 이름")
    private String representativeName;

    @Schema(description = "사업자 번호")
    private String businessNumber;

    @Schema(description = "오픈 일자")
    private LocalDate openingDate;

    @Schema(description = "통신판매번호")
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
