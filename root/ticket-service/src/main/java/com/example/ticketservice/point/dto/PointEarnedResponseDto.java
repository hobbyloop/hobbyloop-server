package com.example.ticketservice.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointEarnedResponseDto {
    private Long amount;
    private Long balance;
    private LocalDateTime expirationDateTime;
}
