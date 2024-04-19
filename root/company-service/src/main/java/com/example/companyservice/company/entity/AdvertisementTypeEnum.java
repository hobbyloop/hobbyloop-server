package com.example.companyservice.company.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AdvertisementTypeEnum {

    CPC(1, "CPC"),
    CPM(2, "CPM"),
    BANNER(3, "배너광고");

    private final int typeValue;

    private final String name;

    public static AdvertisementTypeEnum findByValue(final int typeValue) {
        return Arrays.stream(AdvertisementTypeEnum.values())
                .filter(e -> e.typeValue == typeValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 광고 타입 입니다."));
    }

    public static AdvertisementTypeEnum findByName(final String name) {
        return Arrays.stream(AdvertisementTypeEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 광고 타입 입니다."));
    }
}
