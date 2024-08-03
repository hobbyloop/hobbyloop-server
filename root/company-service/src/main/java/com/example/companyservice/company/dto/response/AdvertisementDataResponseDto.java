package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementDataResponseDto {

    public int clickCount;

    public int selectCount;

    public double clickRate;

    public int month;

    public static AdvertisementDataResponseDto of(int clickCount,
                                                  int selectCount,
                                                  double clickRate,
                                                  int month) {
        return AdvertisementDataResponseDto.builder()
                .clickCount(clickCount)
                .selectCount(selectCount)
                .clickRate(clickRate)
                .month(month)
                .build();
    }
}
