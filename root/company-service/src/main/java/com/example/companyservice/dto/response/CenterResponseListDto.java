package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Center;
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

    public static CenterResponseListDto of(LocalDate startAt, LocalDate endAt, List<Center> centerList) {
        return CenterResponseListDto.builder()
                .startAt(startAt)
                .endAt(endAt)
                .centerResponseDtoList(centerList.stream().map(CenterResponseDto::from).toList())
                .build();
    }
}
