package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "시설 프로필 정보")
public class CenterResponseDto {

    @Schema(description = "시설 아이디", example = "1")
    private Long centerId;

    @Schema(description = "시설 이름", example = "필라피티 스튜디오")
    private String centerName;

    @Schema(description = "로고 이미지 url")
    private String logoImageUrl;

    public static CenterResponseDto from(Center center) {
        return CenterResponseDto.builder()
                .centerId(center.getId())
                .centerName(center.getCenterName())
                .logoImageUrl(center.getLogoImageUrl())
                .build();
    }
}
