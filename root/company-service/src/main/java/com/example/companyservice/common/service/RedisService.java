package com.example.companyservice.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 키-벨류 설정
    public void setValues(String token, String subject){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(subject, token, Duration.ofMinutes(14 * 24 * 60));
    }

    // 키값으로 벨류 가져오기
    public String getValues(String subject){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(subject);
    }

    // 키-벨류 삭제
    public void delValues(String subject) {
        redisTemplate.delete(subject);
    }
}