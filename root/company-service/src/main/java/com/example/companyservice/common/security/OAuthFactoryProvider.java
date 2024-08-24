package com.example.companyservice.common.security;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuthFactoryProvider {

    private final Map<ProviderType, OAuthFactory> factoryMap = new HashMap<>();

    @Autowired
    public OAuthFactoryProvider(KakaoFactory kakaoFactory, GoogleFactory googleFactory, NaverFactory naverFactory) {
        factoryMap.put(ProviderType.KAKAO, kakaoFactory);
        factoryMap.put(ProviderType.GOOGLE, googleFactory);
        factoryMap.put(ProviderType.NAVER, naverFactory);
    }

    public OAuthFactory getFactory(ProviderType providerType) {
        return Optional.ofNullable(factoryMap.get(providerType))
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOT_SUPPORT_PROVIDER_TYPE));
    }
}
