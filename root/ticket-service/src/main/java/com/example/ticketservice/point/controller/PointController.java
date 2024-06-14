package com.example.ticketservice.point.controller;

import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.example.ticketservice.point.service.PointService;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointController {

    private final PointService pointService;

    @GetMapping("/histories")
    public ResponseEntity<BaseResponseDto<PointHistoryListResponseDto>> getPointHistories(HttpServletRequest request) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(pointService.getPointHistory(memberId)));
    }
}
