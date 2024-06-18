package com.example.ticketservice.point.controller;

import com.example.ticketservice.point.dto.PointEarnedResponseDto;
import com.example.ticketservice.point.service.PointService;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points/client")
public class PointClientController {
    private final PointService pointService;

    @PostMapping("/join/{memberId}")
    public ResponseEntity<BaseResponseDto<PointEarnedResponseDto>> join(@PathVariable(value = "memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(pointService.earnPointWhenJoining(memberId)));
    }

    @GetMapping("/my/{memberId}")
    public ResponseEntity<BaseResponseDto<Long>> getMyTotalPoints(@PathVariable(value = "memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(pointService.getMemberTotalPoints(memberId)));
    }
}
