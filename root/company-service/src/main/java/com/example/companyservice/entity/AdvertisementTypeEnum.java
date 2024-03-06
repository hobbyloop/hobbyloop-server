package com.example.companyservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AdvertisementTypeEnum {

    CPC(1),
    CPM(2),
    배너광고(3);

    private final int typeValue;

    public static AdvertisementTypeEnum findByValue(final int typeValue) {
        return Arrays.stream(AdvertisementTypeEnum.values())
                .filter(e -> e.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 광고 타입 입니다."));
    }
}
