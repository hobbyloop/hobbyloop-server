package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "시설 등록 요청 바디")
public class CenterCreateRequestDto {

    @NotBlank
    @Schema(description = "시설 이름", example = "필라피티 스튜디오", required = true)
    private String centerName;

    @NotBlank
    @Schema(description = "시설 주소", example = "서울 강남구 압구정로50길 8 2층", required = true)
    private String address;

    @Schema(description = "공지사항")
    private String announcement;

    @Schema(description = "시설 소개")
    private String introduce;

    @NotBlank
    @Schema(description = "문의 연락처", example = "010-0000-0000", required = true)
    private String contact;

    @NotBlank
    @Schema(description = "오픈 카카오톡 문의 링크", required = true)
    private String kakaoLink;

    @Schema(description = "운영 시간 리스트", required = true)
    private List<HourRequestDto> operatingHourList;

    @Schema(description = "휴게 시간 리스트")
    private List<HourRequestDto> breakHourList;

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

    @NotNull
    @Schema(description = "위도", example = "00.000000", required = true)
    private double latitude;

    @NotNull
    @Schema(description = "경도", example = "00.000000", required = true)
    private double longitude;
}
