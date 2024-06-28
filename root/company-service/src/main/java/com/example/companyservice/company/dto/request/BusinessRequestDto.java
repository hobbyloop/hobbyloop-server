package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(title = "사업자 정보 수정 요청 바디")
public class BusinessRequestDto {

    @NotBlank
    @Schema(description = "대표자 이름", example = "김하비", required = true)
    private String representativeName;

    @NotBlank
    @Schema(description = "사업자 번호", example = "84305803480", required = true)
    private String businessNumber;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "개업 날짜", example = "2024-01-01", required = true)
    private LocalDate openingDate;

    @Schema(description = "통신판매번호")
    private String onlineReportNumber;

    @NotBlank
    @Schema(description = "은행 이름", example = "농협", required = true)
    private String accountBank;

    @NotBlank
    @Schema(description = "계좌번호", required = true)
    private String accountNumber;
}
