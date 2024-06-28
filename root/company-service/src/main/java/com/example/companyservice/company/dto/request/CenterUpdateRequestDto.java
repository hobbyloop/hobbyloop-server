package com.example.companyservice.company.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(title = "시설 등록 수정 요청 바디")
public class CenterUpdateRequestDto {

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

    @NotNull
    @Schema(description = "위도", example = "00.000000", required = true)
    private double latitude;

    @NotNull
    @Schema(description = "경도", example = "00.000000", required = true)
    private double longitude;
}
