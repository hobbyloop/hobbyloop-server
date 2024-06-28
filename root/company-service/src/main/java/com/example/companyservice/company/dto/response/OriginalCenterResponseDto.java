package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "수정전 시설 정보 응답 바디")
public class OriginalCenterResponseDto {

    @Schema(description = "시설 이름", example = "필라피티 스튜디오")
    private String centerName;

    @Schema(description = "시설 주소", example = "서울 강남구 압구정로50길 8 2층")
    private String address;

    @Schema(description = "로고 이미지 url")
    private String logoImageUrl;

    @Schema(description = "시설 소개 이미지 리스트")
    private List<String> centerImageUrl;

    @Schema(description = "공지사항")
    private String announcement;

    @Schema(description = "시설 소개")
    private String introduce;

    @Schema(description = "연락처", example = "010-0000-0000")
    private String contact;

    @Schema(description = "오픈 카카오톡 링크")
    private String kakaoLink;

    @Schema(description = "운영 시간 리스트")
    private List<HourResponseDto> operatingHourList;

    @Schema(description = "휴게 시간 리스트")
    private List<HourResponseDto> breakHourList;

    public static OriginalCenterResponseDto of(Center center,
                                               List<String> centerImageUrl,
                                               List<HourResponseDto> operatingHourList,
                                               List<HourResponseDto> breakHourList) {
        return OriginalCenterResponseDto.builder()
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .logoImageUrl(center.getLogoImageUrl())
                .centerImageUrl(centerImageUrl)
                .announcement(center.getAnnouncement())
                .introduce(center.getIntroduce())
                .contact(center.getContact())
                .kakaoLink(center.getKakaoLink())
                .operatingHourList(operatingHourList)
                .breakHourList(breakHourList)
                .build();
    }
}
