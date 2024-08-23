package com.example.companyservice.common.security;


import com.example.companyservice.company.entity.enumerated.AuthorityEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProviderType {
    KAKAO("Kakao"),
    GOOGLE("Google"),
    NAVER("Naver"),
    APPLE("Apple");

    private String providerName;

    public static ProviderType findByName(final String name) {
        return Arrays.stream(ProviderType.values())
                .filter(e -> e.providerName.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("지원하지 않는 Provider 입니다."));
    }
}
