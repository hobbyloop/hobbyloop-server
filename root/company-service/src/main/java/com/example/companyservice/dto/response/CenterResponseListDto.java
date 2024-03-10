package com.example.companyservice.dto.response;

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
public class CenterResponseListDto {

    private LocalDate startAt;

    private LocalDate endAt;

    private List<CenterResponseDto> centerResponseDtoList;

    public static CenterResponseListDto of(LocalDate startAt, LocalDate endAt, List<CenterResponseDto> centerResponseDtoList) {
        return CenterResponseListDto.builder()
                .startAt(startAt)
                .endAt(endAt)
                .centerResponseDtoList(centerResponseDtoList)
                .build();
    }
}
