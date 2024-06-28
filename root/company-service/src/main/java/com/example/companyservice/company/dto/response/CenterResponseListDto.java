package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "시설 프로필 응답 바디")
public class CenterResponseListDto {

    @Schema(description = "서비스 이용 시작 기간", example = "2024-01-01")
    private LocalDate startAt;

    @Schema(description = "서비스 이용 종료 기간", example = "2024-12-31")
    private LocalDate endAt;

    @Schema(description = "시설 프로필 리스트")
    private List<CenterResponseDto> centerResponseDtoList;

    public static CenterResponseListDto of(LocalDate startAt, LocalDate endAt, List<Center> centerList) {
        return CenterResponseListDto.builder()
                .startAt(startAt)
                .endAt(endAt)
                .centerResponseDtoList(centerList.stream().map(CenterResponseDto::from).toList())
                .build();
    }
}
