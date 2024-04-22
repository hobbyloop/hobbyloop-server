package com.example.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CenterMembershipStatusEnum {
    ACTIVE(1, "회원"),
    EXPIRING_SOON(2, "만료예정"),
    EXPIRED(3, "만료"),
    RENEWED(4, "재가입");

    private final int statusType;
    private final String name;

    public static CenterMembershipStatusEnum findByValue(final int statusType) {
        return Arrays.stream(CenterMembershipStatusEnum.values())
                .filter(e -> e.statusType == statusType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 카테고리 입니다."));
    }

    public static CenterMembershipStatusEnum findByName(final String name) {
        return Arrays.stream(CenterMembershipStatusEnum.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 카테고리 입니다."));
    }
}
