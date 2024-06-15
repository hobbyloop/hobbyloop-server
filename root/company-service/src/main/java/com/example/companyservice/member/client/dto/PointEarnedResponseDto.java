package com.example.companyservice.member.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PointEarnedResponseDto {
    private Long amount;
    private Long balance;
    private LocalDateTime expirationDateTime;
}

